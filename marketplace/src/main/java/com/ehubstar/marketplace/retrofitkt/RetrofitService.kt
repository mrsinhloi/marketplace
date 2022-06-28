package com.ehubstar.marketplace.retrofitkt

import com.ehubstar.marketplace.models.Movie
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
//https://medium.com/android-beginners/how-to-get-continuous-location-updates-in-android-9bb308d468d3
interface RetrofitService {
    @GET("movielist.json")
    suspend fun getAllMovies(): Response<List<Movie>>

    companion object {
        var retrofitService: RetrofitService? = null
        fun getInstance(): RetrofitService {
            if (retrofitService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://howtodoandroid.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                retrofitService = retrofit.create(RetrofitService::class.java)
            }
            return retrofitService!!
        }

    }
}