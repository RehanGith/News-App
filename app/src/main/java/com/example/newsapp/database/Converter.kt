package com.example.newsapp.database

import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.example.newsapp.model.Source

class Converter {
    @TypeConverter
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverter
    fun toSource(name : String): Source {
        return Source(name , name)
    }
}