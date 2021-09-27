package com.example.squirrelwarehouse

import com.example.squirrelwarehouse.Repo.Companion.BASE_URL
import com.example.squirrelwarehouse.NotiAPI
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
/*
class RetrofitInstnace {
    companion object{
        private val retrofit by lazy{
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        /*
        val api by lazy{
            retrofit.create(NotiAPI::class.java)
        }*/
        val api = retrofit.create(NotiAPI::class.java)
    }*/

class RetrofitInstnace {

    companion object {
        private val retrofit by lazy {
            Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
        }

        val api by lazy {
            retrofit.create(NotiAPI::class.java)
        }
    }
}