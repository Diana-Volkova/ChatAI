package com.example.chatai.data.local

import androidx.room.TypeConverter
import org.json.JSONArray

class MessageConverters {

    @TypeConverter
    fun fromAlternatives(value: List<String>): String {
        return JSONArray(value).toString()
    }

    @TypeConverter
    fun toAlternatives(value: String): List<String> {
        val jsonArray = JSONArray(value)

        return List(jsonArray.length()) { index ->
            jsonArray.getString(index)
        }
    }
}