package com.ehubstar.marketplace.retrofitkt

class MainRepository constructor(private val retrofitService: RetrofitService) {
    suspend fun getAllMovies() = retrofitService.getAllMovies()
}