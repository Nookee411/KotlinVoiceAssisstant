package ru.example.voiceassistentkotlin.weatherAPI

import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.function.Consumer
import kotlin.math.abs

class ForecastToString {
    companion object{
        fun getForecast(city: String, callback:Consumer<String> ){
            val api = ForecastService.getApi()
            val call: Call<Forecast?>? =  api.getCurrentWeather(city)
            call!!.enqueue(object : Callback<Forecast?> {
                override fun onResponse(call: Call<Forecast?>?, response: Response<Forecast?>?) {
                    val result = response?.body()
                    Log.d("API", "onResponse: $response")
                    try {
                        if (result != null) {
                            val answer = formAnswer(result.current.temperature, result.current.weatherDescription[0])
                            callback.accept(answer);
                        } else {
                            callback.accept("Не могу узнать погоду");
                        }
                    } catch (ex:Exception){
                        callback.accept("Погода для данного региона недоступна")
                    }
                }

                override fun onFailure(call: Call<Forecast?>, t: Throwable) {
                    Log.w("WEATHER", t.message.toString());
                }
            })

        }

        fun formAnswer(temp:Int, desc:String):String{
            val absTemp = abs(temp)
            return "сейчас где-то $temp " + when (absTemp%10) {
                1 -> if(absTemp != 11) "градус" else "градусов"
                in 2..4 ->"градуса"
                in 5..9 ->"градусов"
                0 ->"градусов"

                else -> "градус"
            } + " и $desc"

        }
    }
}