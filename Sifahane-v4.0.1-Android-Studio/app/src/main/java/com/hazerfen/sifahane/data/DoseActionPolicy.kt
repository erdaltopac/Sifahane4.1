package com.hazerfen.sifahane.data

/** Android bağımlılığı olmayan karar tablosu; JVM testinde doğrulanır. */
object DoseActionPolicy {
    fun shouldDecreaseStock(latestAction: String?): Boolean = latestAction != DoseAction.TAKEN.storageValue
    fun shouldRestoreStock(stockDecreased: Boolean?, restoreLegacyStock: Boolean): Boolean =
        stockDecreased ?: restoreLegacyStock
    fun canUndo(existingUndoCount: Int): Boolean = existingUndoCount == 0
}
