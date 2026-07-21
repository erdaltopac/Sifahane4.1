package com.hazerfen.sifahane.data

import androidx.room.withTransaction

/** Sonuç, kullanıcı arayüzünün stok etkisini doğru açıklayabilmesi için döndürülür. */
data class DoseActionResult(
    val log: DoseLog,
    val stockChanged: Boolean,
    val duplicateSuppressed: Boolean
)

object DoseActionRepository {
    suspend fun recordTaken(
        db: AppDatabase,
        profileId: Long,
        medicationId: Long,
        medicationName: String,
        scheduledDateTime: Long,
        actualDateTime: Long
    ): DoseActionResult = db.withTransaction {
        val latest = db.doseLogDao().latestForDose(medicationId, scheduledDateTime)
        if (latest != null && !DoseActionPolicy.shouldDecreaseStock(latest.action)) {
            val updated = latest.copy(
                actualDateTime = actualDateTime,
                timestamp = System.currentTimeMillis()
            )
            db.doseLogDao().update(updated)
            return@withTransaction DoseActionResult(updated, stockChanged = false, duplicateSuppressed = true)
        }

        val decreased = db.medicationDao().decreaseStock(medicationId) > 0
        val log = DoseLog(
            profileId = profileId,
            medicationId = medicationId,
            medicationName = medicationName,
            scheduledDateTime = scheduledDateTime,
            actualDateTime = actualDateTime,
            action = DoseAction.TAKEN.storageValue,
            stockDecreased = decreased
        )
        db.doseLogDao().insert(log)
        DoseActionResult(log, stockChanged = decreased, duplicateSuppressed = false)
    }

    suspend fun undoTaken(
        db: AppDatabase,
        takenLog: DoseLog,
        restoreLegacyStock: Boolean
    ): DoseActionResult = db.withTransaction {
        require(takenLog.action == DoseAction.TAKEN.storageValue) { "Yalnız alınmış doz geri alınabilir." }
        val alreadyUndone = db.doseLogDao().undoCountAfter(
            medicationId = takenLog.medicationId,
            scheduledDateTime = takenLog.scheduledDateTime,
            takenTimestamp = takenLog.timestamp
        ) > 0
        if (!DoseActionPolicy.canUndo(if (alreadyUndone) 1 else 0)) {
            return@withTransaction DoseActionResult(
                log = takenLog,
                stockChanged = false,
                duplicateSuppressed = true
            )
        }

        val restoreStock = DoseActionPolicy.shouldRestoreStock(
            takenLog.stockDecreased,
            restoreLegacyStock
        )
        if (restoreStock) db.medicationDao().increaseStock(takenLog.medicationId)
        val undoLog = DoseLog(
            profileId = takenLog.profileId,
            medicationId = takenLog.medicationId,
            medicationName = takenLog.medicationName,
            scheduledDateTime = takenLog.scheduledDateTime,
            actualDateTime = System.currentTimeMillis(),
            action = "ALINDI GERİ ALINDI",
            stockDecreased = false
        )
        db.doseLogDao().insert(undoLog)
        DoseActionResult(undoLog, stockChanged = restoreStock, duplicateSuppressed = false)
    }
}
