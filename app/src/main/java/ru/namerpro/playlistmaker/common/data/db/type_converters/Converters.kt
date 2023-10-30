package ru.namerpro.playlistmaker.common.data.db.type_converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.namerpro.playlistmaker.search.domain.model.TrackModel

class Converters {

    @TypeConverter
    fun toArrayList(
        json: String
    ): ArrayList<Long> {
        return Gson().fromJson(json, object : TypeToken<ArrayList<Long>>() {}.type)
    }

    @TypeConverter
    fun fromArrayList(
        list: ArrayList<Long>
    ): String {
        return Gson().toJson(list)
    }

}