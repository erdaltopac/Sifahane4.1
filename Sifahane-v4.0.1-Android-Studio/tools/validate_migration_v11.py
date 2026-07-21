#!/usr/bin/env python3
"""Static SQLite acceptance test for the Room 10->11 migration contract."""
import sqlite3

conn = sqlite3.connect(':memory:')
conn.execute('PRAGMA foreign_keys=OFF')
conn.executescript('''
CREATE TABLE profiles (
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
 name TEXT NOT NULL, relation TEXT NOT NULL, surname TEXT NOT NULL,
 photoUri TEXT, birthDate TEXT, bloodGroup TEXT NOT NULL, profileNote TEXT NOT NULL,
 role TEXT NOT NULL, adminPinHash TEXT, permissionsCsv TEXT NOT NULL, accountEnabled INTEGER NOT NULL
);
CREATE TABLE report_groups (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, name TEXT NOT NULL, startDate TEXT NOT NULL, endDate TEXT NOT NULL, warningDays INTEGER NOT NULL);
CREATE TABLE medications (
 id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, name TEXT NOT NULL,
 purpose TEXT NOT NULL, dose TEXT NOT NULL, timesCsv TEXT NOT NULL, stock INTEGER NOT NULL,
 lowStockLimit INTEGER NOT NULL, photoUri TEXT, notes TEXT NOT NULL, startDate TEXT NOT NULL,
 endDate TEXT, continuous INTEGER NOT NULL, active INTEGER NOT NULL, archived INTEGER NOT NULL,
 barcode TEXT, prospectusUrl TEXT, doctorName TEXT NOT NULL, doctorBranch TEXT NOT NULL,
 doctorInstitution TEXT NOT NULL, doctorPhone TEXT NOT NULL, isReported INTEGER NOT NULL,
 reportStartDate TEXT, reportEndDate TEXT, reportWarningDays INTEGER NOT NULL, reportGroupId INTEGER
);
CREATE TABLE dose_logs (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, medicationId INTEGER NOT NULL, medicationName TEXT NOT NULL, scheduledDateTime INTEGER NOT NULL, actualDateTime INTEGER, action TEXT NOT NULL, timestamp INTEGER NOT NULL, stockDecreased INTEGER);
CREATE TABLE blood_pressure (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, systolic INTEGER NOT NULL, diastolic INTEGER NOT NULL, pulse INTEGER, measuredAt INTEGER NOT NULL, note TEXT NOT NULL);
CREATE TABLE blood_glucose (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, valueMgDl INTEGER NOT NULL, measurementType TEXT NOT NULL, measuredAt INTEGER NOT NULL, note TEXT NOT NULL);
CREATE TABLE appointments (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, doctorName TEXT NOT NULL, branch TEXT NOT NULL, institution TEXT NOT NULL, clinic TEXT NOT NULL, appointmentDateTime INTEGER NOT NULL, phone TEXT NOT NULL, address TEXT NOT NULL, note TEXT NOT NULL, status TEXT NOT NULL, remindersCsv TEXT NOT NULL, active INTEGER NOT NULL, source TEXT NOT NULL, createdAt INTEGER NOT NULL);
''')
conn.execute("INSERT INTO profiles VALUES (1,'Erdal','','Topaç',NULL,NULL,'Bilinmiyor','','ADMIN',NULL,'ALL',1)")
conn.execute("INSERT INTO report_groups VALUES (1,1,'Rapor','2026-01-01','2026-12-31',7)")
conn.execute("INSERT INTO report_groups VALUES (2,999,'Yetim','2026-01-01','2026-12-31',7)")
med = "INSERT INTO medications VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)"
conn.execute(med,(1,1,'İlaç','','1 tablet','08:00',-3,-2,None,'','2026-01-01',None,1,1,0,None,None,'','','','',0,None,None,7,1))
conn.execute(med,(2,999,'Yetim','','1','09:00',1,1,None,'','2026-01-01',None,1,1,0,None,None,'','','','',0,None,None,7,None))
conn.execute("INSERT INTO dose_logs VALUES (1,1,1,'İlaç',1,NULL,'ALINDI',2,1)")
conn.execute("INSERT INTO dose_logs VALUES (2,999,2,'Yetim',1,NULL,'ALINDI',2,1)")
conn.execute("INSERT INTO blood_pressure VALUES (1,1,120,80,70,1,'')")
conn.execute("INSERT INTO blood_pressure VALUES (2,999,120,80,70,1,'')")
conn.execute("INSERT INTO blood_glucose VALUES (1,1,100,'AÇLIK',1,'')")
conn.execute("INSERT INTO appointments VALUES (1,1,'Dr','','','','', '', '', '', 'PLANNED','180',1,'MANUAL',1)")
# Fix wrong-shaped appointment seed caused by explicit column count using a clear insert.
conn.execute('DELETE FROM appointments')
conn.execute("INSERT INTO appointments(id,profileId,doctorName,branch,institution,clinic,appointmentDateTime,phone,address,note,status,remindersCsv,active,source,createdAt) VALUES (1,1,'Dr','','','',1000,'','','','PLANNED','180',1,'MANUAL',1)")

conn.executescript('''
CREATE TABLE report_groups_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, name TEXT NOT NULL, startDate TEXT NOT NULL, endDate TEXT NOT NULL, warningDays INTEGER NOT NULL, FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE);
INSERT INTO report_groups_new SELECT rg.id,rg.profileId,rg.name,rg.startDate,rg.endDate,rg.warningDays FROM report_groups rg INNER JOIN profiles p ON p.id=rg.profileId;
DROP TABLE report_groups; ALTER TABLE report_groups_new RENAME TO report_groups;
CREATE INDEX index_report_groups_profileId ON report_groups(profileId);
CREATE INDEX index_report_groups_profileId_name ON report_groups(profileId,name);
CREATE TABLE medications_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, name TEXT NOT NULL, purpose TEXT NOT NULL, dose TEXT NOT NULL, timesCsv TEXT NOT NULL, stock INTEGER NOT NULL, lowStockLimit INTEGER NOT NULL, photoUri TEXT, notes TEXT NOT NULL, startDate TEXT NOT NULL, endDate TEXT, continuous INTEGER NOT NULL, active INTEGER NOT NULL, archived INTEGER NOT NULL, barcode TEXT, prospectusUrl TEXT, doctorName TEXT NOT NULL, doctorBranch TEXT NOT NULL, doctorInstitution TEXT NOT NULL, doctorPhone TEXT NOT NULL, isReported INTEGER NOT NULL, reportStartDate TEXT, reportEndDate TEXT, reportWarningDays INTEGER NOT NULL, reportGroupId INTEGER, FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE, FOREIGN KEY(reportGroupId) REFERENCES report_groups(id) ON DELETE SET NULL);
INSERT INTO medications_new SELECT m.id,m.profileId,m.name,m.purpose,m.dose,m.timesCsv,CASE WHEN m.stock<0 THEN 0 ELSE m.stock END,CASE WHEN m.lowStockLimit<0 THEN 0 ELSE m.lowStockLimit END,m.photoUri,m.notes,m.startDate,m.endDate,m.continuous,m.active,m.archived,m.barcode,m.prospectusUrl,m.doctorName,m.doctorBranch,m.doctorInstitution,m.doctorPhone,m.isReported,m.reportStartDate,m.reportEndDate,m.reportWarningDays,CASE WHEN rg.id IS NULL OR rg.profileId!=m.profileId THEN NULL ELSE rg.id END FROM medications m INNER JOIN profiles p ON p.id=m.profileId LEFT JOIN report_groups rg ON rg.id=m.reportGroupId;
DROP TABLE medications; ALTER TABLE medications_new RENAME TO medications;
CREATE INDEX index_medications_profileId ON medications(profileId); CREATE INDEX index_medications_reportGroupId ON medications(reportGroupId); CREATE INDEX index_medications_profileId_active_archived ON medications(profileId,active,archived); CREATE INDEX index_medications_name ON medications(name);
CREATE TABLE dose_logs_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, medicationId INTEGER NOT NULL, medicationName TEXT NOT NULL, scheduledDateTime INTEGER NOT NULL, actualDateTime INTEGER, action TEXT NOT NULL, stockDecreased INTEGER, timestamp INTEGER NOT NULL, FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE, FOREIGN KEY(medicationId) REFERENCES medications(id) ON DELETE NO ACTION);
INSERT INTO dose_logs_new(id,profileId,medicationId,medicationName,scheduledDateTime,actualDateTime,action,stockDecreased,timestamp) SELECT d.id,d.profileId,d.medicationId,d.medicationName,d.scheduledDateTime,d.actualDateTime,d.action,d.stockDecreased,d.timestamp FROM dose_logs d INNER JOIN profiles p ON p.id=d.profileId INNER JOIN medications m ON m.id=d.medicationId AND m.profileId=d.profileId;
DROP TABLE dose_logs; ALTER TABLE dose_logs_new RENAME TO dose_logs;
CREATE INDEX index_dose_logs_profileId ON dose_logs(profileId); CREATE INDEX index_dose_logs_medicationId ON dose_logs(medicationId); CREATE INDEX index_dose_logs_scheduledDateTime ON dose_logs(scheduledDateTime); CREATE INDEX index_dose_logs_profileId_timestamp ON dose_logs(profileId,timestamp);
''')
for table, defs, names in [
 ('blood_pressure','systolic INTEGER NOT NULL, diastolic INTEGER NOT NULL, pulse INTEGER','systolic,diastolic,pulse'),
 ('blood_glucose','valueMgDl INTEGER NOT NULL, measurementType TEXT NOT NULL','valueMgDl,measurementType')]:
    conn.executescript(f'''CREATE TABLE {table}_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, {defs}, measuredAt INTEGER NOT NULL, note TEXT NOT NULL, FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE);
    INSERT INTO {table}_new(id,profileId,{names},measuredAt,note) SELECT v.id,v.profileId,{','.join('v.'+x for x in names.split(','))},v.measuredAt,v.note FROM {table} v INNER JOIN profiles p ON p.id=v.profileId;
    DROP TABLE {table}; ALTER TABLE {table}_new RENAME TO {table}; CREATE INDEX index_{table}_profileId ON {table}(profileId); CREATE INDEX index_{table}_measuredAt ON {table}(measuredAt); CREATE INDEX index_{table}_profileId_measuredAt ON {table}(profileId,measuredAt);''')
conn.executescript('''CREATE TABLE appointments_new (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, profileId INTEGER NOT NULL, doctorName TEXT NOT NULL, branch TEXT NOT NULL, institution TEXT NOT NULL, clinic TEXT NOT NULL, appointmentDateTime INTEGER NOT NULL, phone TEXT NOT NULL, address TEXT NOT NULL, note TEXT NOT NULL, status TEXT NOT NULL, remindersCsv TEXT NOT NULL, active INTEGER NOT NULL, source TEXT NOT NULL, createdAt INTEGER NOT NULL, FOREIGN KEY(profileId) REFERENCES profiles(id) ON DELETE CASCADE);
INSERT INTO appointments_new SELECT a.* FROM appointments a INNER JOIN profiles p ON p.id=a.profileId; DROP TABLE appointments; ALTER TABLE appointments_new RENAME TO appointments; CREATE INDEX index_appointments_profileId ON appointments(profileId); CREATE INDEX index_appointments_appointmentDateTime ON appointments(appointmentDateTime); CREATE INDEX index_appointments_profileId_active_status_appointmentDateTime ON appointments(profileId,active,status,appointmentDateTime);''')
conn.execute('PRAGMA foreign_keys=ON')
assert conn.execute('PRAGMA foreign_key_check').fetchall() == []
assert conn.execute('SELECT COUNT(*) FROM report_groups').fetchone()[0] == 1
assert conn.execute('SELECT COUNT(*) FROM medications').fetchone()[0] == 1
assert conn.execute('SELECT stock,lowStockLimit FROM medications').fetchone() == (0,0)
assert conn.execute('SELECT COUNT(*) FROM dose_logs').fetchone()[0] == 1
assert conn.execute('SELECT COUNT(*) FROM blood_pressure').fetchone()[0] == 1
# Medication history is protected by an explicit NO ACTION relationship.
try:
    conn.execute('DELETE FROM medications WHERE id=1')
    raise AssertionError('medication delete unexpectedly succeeded while history existed')
except sqlite3.IntegrityError:
    conn.rollback()
# Cascade acceptance
conn.execute('DELETE FROM profiles WHERE id=1')
for table in ('report_groups','medications','dose_logs','blood_pressure','blood_glucose','appointments'):
    assert conn.execute(f'SELECT COUNT(*) FROM {table}').fetchone()[0] == 0, table
print('MIGRATION_10_11_SQLITE_OK')
