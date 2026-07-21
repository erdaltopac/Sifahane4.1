#!/usr/bin/env python3
"""Generate deterministic Room schema snapshots for supported database versions 3..11."""
from __future__ import annotations
import copy, hashlib, json
from pathlib import Path

DB_NAME = "com.hazerfen.sifahane.data.AppDatabase"
OUT = Path("app/schemas") / DB_NAME


def field(name, affinity, not_null, path=None):
    return {"fieldPath": path or name, "columnName": name, "affinity": affinity, "notNull": not_null}


def entity(table, fields, indices=None, foreign_keys=None):
    cols = []
    for f in fields:
        col = f"`{f['columnName']}` {f['affinity']}"
        if f["notNull"]:
            col += " NOT NULL"
        cols.append(col)
    cols[0] += " PRIMARY KEY AUTOINCREMENT"
    create = f"CREATE TABLE IF NOT EXISTS `${{TABLE_NAME}}` ({', '.join(cols)})"
    idx = []
    for columns in indices or []:
        unique = False
        if isinstance(columns, dict):
            unique = bool(columns.get("unique"))
            columns = columns["columns"]
        name = f"index_{table}_{'_'.join(columns)}"
        unique_sql = "UNIQUE " if unique else ""
        idx.append({
            "name": name,
            "unique": unique,
            "columnNames": columns,
            "orders": [],
            "createSql": f"CREATE {unique_sql}INDEX IF NOT EXISTS `{name}` ON `${{TABLE_NAME}}` ({', '.join(f'`{c}`' for c in columns)})"
        })
    return {
        "tableName": table,
        "createSql": create,
        "fields": fields,
        "primaryKey": {"autoGenerate": True, "columnNames": ["id"]},
        "indices": idx,
        "foreignKeys": foreign_keys or []
    }


def fk(table, column, on_delete):
    return {
        "table": table,
        "onDelete": on_delete,
        "onUpdate": "NO ACTION",
        "columns": [column],
        "referencedColumns": ["id"]
    }

profiles_v3 = [field("id","INTEGER",True), field("name","TEXT",True), field("relation","TEXT",True)]
med_v3 = [
    field("id","INTEGER",True), field("profileId","INTEGER",True), field("name","TEXT",True),
    field("purpose","TEXT",True), field("dose","TEXT",True), field("timesCsv","TEXT",True),
    field("stock","INTEGER",True), field("lowStockLimit","INTEGER",True), field("photoUri","TEXT",False),
    field("notes","TEXT",True), field("startDate","TEXT",True), field("endDate","TEXT",False),
    field("continuous","INTEGER",True), field("active","INTEGER",True)
]
dose_v3 = [
    field("id","INTEGER",True), field("profileId","INTEGER",True), field("medicationId","INTEGER",True),
    field("medicationName","TEXT",True), field("scheduledDateTime","INTEGER",True),
    field("actualDateTime","INTEGER",False), field("action","TEXT",True), field("timestamp","INTEGER",True)
]
bp = [field("id","INTEGER",True), field("profileId","INTEGER",True), field("systolic","INTEGER",True), field("diastolic","INTEGER",True), field("pulse","INTEGER",False), field("measuredAt","INTEGER",True), field("note","TEXT",True)]
glucose = [field("id","INTEGER",True), field("profileId","INTEGER",True), field("valueMgDl","INTEGER",True), field("measurementType","TEXT",True), field("measuredAt","INTEGER",True), field("note","TEXT",True)]
report_groups = [field("id","INTEGER",True), field("profileId","INTEGER",True), field("name","TEXT",True), field("startDate","TEXT",True), field("endDate","TEXT",True), field("warningDays","INTEGER",True)]
appointments = [field("id","INTEGER",True), field("profileId","INTEGER",True), field("doctorName","TEXT",True), field("branch","TEXT",True), field("institution","TEXT",True), field("clinic","TEXT",True), field("appointmentDateTime","INTEGER",True), field("phone","TEXT",True), field("address","TEXT",True), field("note","TEXT",True), field("status","TEXT",True), field("remindersCsv","TEXT",True), field("active","INTEGER",True), field("source","TEXT",True), field("createdAt","INTEGER",True)]


def fields_add(base, *more):
    return copy.deepcopy(base) + [copy.deepcopy(x) for x in more]

versions = {}
for version in range(3, 12):
    profiles = copy.deepcopy(profiles_v3)
    meds = copy.deepcopy(med_v3)
    doses = copy.deepcopy(dose_v3)
    entities = []
    if version >= 4:
        profiles = fields_add(profiles, field("surname","TEXT",True), field("photoUri","TEXT",False))
        meds = fields_add(meds, field("archived","INTEGER",True), field("barcode","TEXT",False), field("prospectusUrl","TEXT",False))
    if version >= 5:
        meds = fields_add(meds, field("doctorName","TEXT",True), field("doctorBranch","TEXT",True), field("doctorInstitution","TEXT",True), field("doctorPhone","TEXT",True), field("isReported","INTEGER",True), field("reportStartDate","TEXT",False), field("reportEndDate","TEXT",False), field("reportWarningDays","INTEGER",True))
    if version >= 6:
        profiles = fields_add(profiles, field("birthDate","TEXT",False), field("bloodGroup","TEXT",True), field("profileNote","TEXT",True))
    if version >= 7:
        meds = fields_add(meds, field("reportGroupId","INTEGER",False))
    if version >= 8:
        profiles = fields_add(profiles, field("role","TEXT",True), field("adminPinHash","TEXT",False), field("permissionsCsv","TEXT",True), field("accountEnabled","INTEGER",True))
    if version >= 10:
        # Room field order follows the data class: stockDecreased before timestamp.
        doses = doses[:-1] + [field("stockDecreased","INTEGER",False), doses[-1]]

    if version < 11:
        entities.append(entity("profiles", profiles))
        entities.append(entity("medications", meds))
        entities.append(entity("dose_logs", doses))
        entities.append(entity("blood_pressure", bp))
        entities.append(entity("blood_glucose", glucose))
        if version >= 7:
            entities.append(entity("report_groups", report_groups))
        if version >= 9:
            entities.append(entity("appointments", appointments))
    else:
        entities.append(entity("profiles", profiles))
        entities.append(entity("medications", meds,
            indices=[["profileId"],["reportGroupId"],["profileId","active","archived"],["name"]],
            foreign_keys=[fk("profiles","profileId","CASCADE"), fk("report_groups","reportGroupId","SET NULL")]))
        entities.append(entity("dose_logs", doses,
            indices=[["profileId"],["medicationId"],["scheduledDateTime"],["profileId","timestamp"]],
            foreign_keys=[fk("profiles","profileId","CASCADE"), fk("medications","medicationId","NO ACTION")]))
        entities.append(entity("blood_pressure", bp,
            indices=[["profileId"],["measuredAt"],["profileId","measuredAt"]],
            foreign_keys=[fk("profiles","profileId","CASCADE")]))
        entities.append(entity("blood_glucose", glucose,
            indices=[["profileId"],["measuredAt"],["profileId","measuredAt"]],
            foreign_keys=[fk("profiles","profileId","CASCADE")]))
        entities.append(entity("report_groups", report_groups,
            indices=[["profileId"],["profileId","name"]],
            foreign_keys=[fk("profiles","profileId","CASCADE")]))
        entities.append(entity("appointments", appointments,
            indices=[["profileId"],["appointmentDateTime"],["profileId","active","status","appointmentDateTime"]],
            foreign_keys=[fk("profiles","profileId","CASCADE")]))

    canonical = json.dumps(entities, ensure_ascii=False, sort_keys=True, separators=(",", ":"))
    identity = hashlib.sha256((str(version)+canonical).encode()).hexdigest()[:32]
    versions[version] = {
        "formatVersion": 1,
        "database": {
            "version": version,
            "identityHash": identity,
            "entities": entities,
            "views": [],
            "setupQueries": [
                "CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)",
                f"INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '{identity}')"
            ]
        }
    }

OUT.mkdir(parents=True, exist_ok=True)
for version, payload in versions.items():
    (OUT / f"{version}.json").write_text(json.dumps(payload, ensure_ascii=False, indent=2) + "\n")
print(f"ROOM_SCHEMAS_WRITTEN={len(versions)}")
