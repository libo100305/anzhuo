package com.scanner.phonenumber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "recognition_history")
data class RecognitionHistory(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val recognizedText: String,
    val phoneNumber: String,
    val matchedContactName: String?,
    val timestamp: Long = System.currentTimeMillis()
)