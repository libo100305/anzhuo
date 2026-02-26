package com.scanner.phonenumber.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey val id: String,
    val name: String,
    val phoneNumber: String,
    val normalizedNumber: String // 标准化后的号码用于匹配
) {
    companion object {
        fun fromSystemContact(id: String, name: String, phoneNumber: String): Contact {
            return Contact(
                id = id,
                name = name,
                phoneNumber = phoneNumber,
                normalizedNumber = phoneNumber.replace(Regex("[\\s\\-\\(\\)]"), "")
            )
        }
    }
}