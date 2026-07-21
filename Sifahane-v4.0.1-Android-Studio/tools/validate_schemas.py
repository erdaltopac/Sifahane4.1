#!/usr/bin/env python3
import json
from pathlib import Path

root = Path('app/schemas/com.hazerfen.sifahane.data.AppDatabase')
expected = set(range(3, 12))
found = {int(p.stem) for p in root.glob('*.json')}
assert found == expected, (found, expected)
for version in sorted(found):
    payload = json.loads((root / f'{version}.json').read_text())
    db = payload['database']
    assert db['version'] == version
    assert db['identityHash'] and len(db['identityHash']) == 32
    assert db['entities']

v11 = json.loads((root / '11.json').read_text())['database']
by_table = {e['tableName']: e for e in v11['entities']}
assert set(by_table) == {'profiles','medications','dose_logs','blood_pressure','blood_glucose','report_groups','appointments'}
med_fks = {(x['table'], tuple(x['columns']), x['onDelete']) for x in by_table['medications']['foreignKeys']}
assert ('profiles', ('profileId',), 'CASCADE') in med_fks
assert ('report_groups', ('reportGroupId',), 'SET NULL') in med_fks
log_fks = {(x['table'], tuple(x['columns']), x['onDelete']) for x in by_table['dose_logs']['foreignKeys']}
assert ('profiles', ('profileId',), 'CASCADE') in log_fks
assert ('medications', ('medicationId',), 'NO ACTION') in log_fks
indices = {i['name'] for e in by_table.values() for i in e['indices']}
for required in {
    'index_medications_profileId_active_archived',
    'index_medications_reportGroupId',
    'index_dose_logs_medicationId',
    'index_dose_logs_scheduledDateTime',
    'index_blood_pressure_measuredAt',
    'index_blood_glucose_measuredAt',
    'index_appointments_profileId_active_status_appointmentDateTime'
}:
    assert required in indices, required
print('ROOM_SCHEMA_SNAPSHOTS_OK')
