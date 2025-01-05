package com.example.newsapp.database

import androidx.room.TypeConverters
import com.example.newsapp.model.Source

class Converter {
    @TypeConverters
    fun fromSource(source: Source): String {
        return source.name
    }

    @TypeConverters
    fun toSource(name : String): Source {
        return Source(name , name)
    }
}