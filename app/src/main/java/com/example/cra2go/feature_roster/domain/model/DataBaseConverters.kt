package com.example.cra2go.feature_roster.domain.model

import android.icu.util.GregorianCalendar
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.cra2go.feature_roster.domain.utils.EventAttributes
import com.example.cra2go.feature_roster.domain.utils.Links
import com.google.gson.Gson
import java.util.Date

class DataBaseConverters {

        @TypeConverter
        fun ToCalender(value: String?): Date? {
            val gson = Gson()
            return gson.fromJson(value, Date::class.java)
        }

        @TypeConverter
        fun fromCalender(value: Date?): String? {
            val gson = Gson()
            return gson.toJson(value)
        }

        @TypeConverter
        fun ToLinks(value: String?): Links? {
            val gson = Gson()
            return gson.fromJson(value, Links::class.java)
        }

        @TypeConverter
        fun fromLinks(value: Links?): String? {
            val gson = Gson()
            return gson.toJson(value)
        }

        @TypeConverter
        fun ToAttributes(value: String?): EventAttributes? {
            val gson = Gson()
            return gson.fromJson(value, EventAttributes::class.java)
        }

        @TypeConverter
        fun fromAttributesvalue(value: EventAttributes?): String? {
            val gson = Gson()
            return gson.toJson(value)
        }
}
