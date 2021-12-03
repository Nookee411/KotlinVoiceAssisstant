package ru.example.voiceassistentkotlin.weatherAPI

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface ForecastAPI {
    @GET("/current?access_key=e386abafe70a4212263c9fb7dfdc4212")
    fun getCurrentWeather(@Query("query") city: String?): Call<Forecast?>?

}