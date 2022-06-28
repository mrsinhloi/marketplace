package com.ehubstar.marketplace

import com.ehubstar.marketplace.retrofit.ApiManager

public class MarketplaceSingleton(var urlApi: String) {

    companion object {
        //api singleton
        @Volatile
        private lateinit var apiManager: ApiManager

        //singleton
        @Volatile
        private lateinit var instance: MarketplaceSingleton
        public fun getIntance(url: String): MarketplaceSingleton {
            synchronized(this) {
                if (!::instance.isInitialized || url != this.instance.urlApi) {
                    instance = MarketplaceSingleton(url)
                    apiManager = ApiManager.getIntance(url)
                }
                return instance
            }
        }

    }

    //functions
    public fun getApiManager(): ApiManager {
        return apiManager
    }

}