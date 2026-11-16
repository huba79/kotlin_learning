package com.example.todolist.data

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromListStatus(status: ListStatus): String {
        return status.name
    }

    @TypeConverter
    fun toListStatus(status: String): ListStatus {
        return try {
            ListStatus.valueOf(status)
        } catch (e: Exception) {
            ListStatus.TODO
        }
    }
}
