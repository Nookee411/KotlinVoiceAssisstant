package ru.example.voiceassistentkotlin.weatherAPI

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Forecast:Serializable{
    @SerializedName("current")
    @Expose
    lateinit var current:Weather;
    class Weather{
        @SerializedName("temperature")
        @Expose
        var temperature:Int = 0
        @SerializedName("weather_descriptions")
        @Expose
        lateinit var weatherDescription:List<String>
    }
}