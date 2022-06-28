package com.ehubstar.marketplace.models.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class LocalUtils {
    companion object {
        fun readLocalFromAssets(context: Context): Local {
            lateinit var jsonString: String
            try {
                jsonString = context.assets.open("local.json")
                    .bufferedReader()
                    .use { it.readText() }
            } catch (ioException: IOException) {
                ioException.printStackTrace()
            }

//            val listCountryType = object : TypeToken<ArrayList<LocalItem>>() {}.type
            return Gson().fromJson(jsonString, Local::class.java)
        }
    }
}