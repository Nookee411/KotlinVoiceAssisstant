package ru.example.voiceassistentkotlin.weatherAPI

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ForecastService {
    companion object{
        fun getApi():ForecastAPI{
            val retrofit = Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://api.weatherstack.com/")
            return retrofit.build().create(ForecastAPI::class.java)
        }
    }
}