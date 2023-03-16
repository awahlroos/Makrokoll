package se.umu.cs.oi19aws.makrokoll.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import se.umu.cs.oi19aws.makrokoll.models.IngredientsModel

// Class to convert ArrayLists so it can be stored in the database
class DataConverter {

    inline fun <reified T> Gson.fromJson(json: String) =
        fromJson<T>(json, object : TypeToken<T>() {}.type)

    @TypeConverter
    fun fromArrayListString(list: ArrayList<String>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrayListString(list: String): ArrayList<String> {
        return try {
            Gson().fromJson<ArrayList<String>>(list)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

    @TypeConverter
    fun fromArrayListIngredientsModel(list: ArrayList<IngredientsModel>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toArrayListIngredientsModel(list: String): ArrayList<IngredientsModel> {
        return try {
            Gson().fromJson<ArrayList<IngredientsModel>>(list)
        } catch (e: Exception) {
            arrayListOf()
        }
    }

}