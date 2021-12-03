package com.example.kotlinpractive

import android.util.Log
import ru.example.voiceassistentkotlin.parser.ParsingHTMLService
import ru.example.voiceassistentkotlin.weatherAPI.ForecastToString
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.function.Consumer
import java.util.regex.Matcher
import java.util.regex.Pattern


val TAG = "AI"

class AI {
    companion object {
        val map = mapOf(
            "привет" to MESSAGE_TYPE.GREET,
            "как дела" to MESSAGE_TYPE.HAY,
            "чем занимаешься" to MESSAGE_TYPE.WYD,
            "какой сегодня день" to MESSAGE_TYPE.WHAT_DAY,
            "который час" to MESSAGE_TYPE.WHAT_HOUR,
            "сколько дней до" to MESSAGE_TYPE.REMAIN,
            "погода в" to MESSAGE_TYPE.WEATHER,
            "праздник" to MESSAGE_TYPE.HOLIDAY,
        )
        private val response = Array(1) {""}

        fun getAnswer(question: String, callback: Consumer<String>) {
            val clearQuestion = processQuestion(question);
            val date = Calendar.getInstance(Locale("ru_RU"))

            var questionType = MESSAGE_TYPE.UNKNOWN
            map.keys.forEach{ key ->
                if (clearQuestion.contains(key)) questionType =
                    map[key] ?: error("No such message type")
            }
            when (questionType){
                MESSAGE_TYPE.GREET -> response[0] = "Привет"
                MESSAGE_TYPE.WYD -> response[0] = "Отвечаю на вопросы"
                MESSAGE_TYPE.HAY -> response[0] = "Отлично"
                MESSAGE_TYPE.WHAT_DAY -> response[0] = date.get(Calendar.DAY_OF_MONTH).toString()
                MESSAGE_TYPE.WHAT_HOUR -> response[0] =
                    "${formatTime(date.get(Calendar.HOUR_OF_DAY))}:${
                        formatTime(
                            date.get(
                                Calendar.MINUTE
                            )
                        )
                    }"
                MESSAGE_TYPE.WHAT_DAY_OF_WEEK -> response[0] =
                    date.get(Calendar.DAY_OF_WEEK).toString()
                MESSAGE_TYPE.REMAIN -> response[0] = getDaysUntil(question)
                MESSAGE_TYPE.UNKNOWN -> response[0] = "Не знаю, что сказать"
                MESSAGE_TYPE.WEATHER -> fetchWeather(question,callback)
                MESSAGE_TYPE.HOLIDAY -> ParsingHTMLService.getHoliday(question)
            }
            val responses = response.reduce { acc, elem -> acc + "${elem}, " }
            callback.accept(responses);
        }

        private fun fetchWeather(question: String, callback: Consumer<String>) {
            val cityPattern: Pattern =
                Pattern.compile("погода в городе (\\p{L}+)", Pattern.CASE_INSENSITIVE)
            val matcher: Matcher = cityPattern.matcher(question)
            if (matcher.find()) {
                val cityName: String? = matcher.group(1)
                if(cityName!=null)
                    ForecastToString.getForecast(cityName, object : Consumer<String> {
                        override fun accept(t: String) {
                            callback.accept(t)
                        }

                    })
            }
        }

        private fun processQuestion(question: String):String{
            val res = question.trim().lowercase(Locale("ru-RU"));
            return res.filter { character -> character.isLetterOrDigit() || character.isWhitespace() || character == ':'}
        }
        private fun formatTime(time: Int):String{
           return if(time<10) "0$time" else time.toString()
        }

        private fun getDaysUntil(question: String):String{
            try {
                val curDate = Date()
                val nextDate = SimpleDateFormat("dd.MM.yyyy", Locale("ru-RU")).parse(
                    question.substring(
                        question.length - 10,
                        question.length
                    )
                )
                val diff = nextDate.time - curDate.time
                if (diff < 0) return "Эта дата уже прошла"
                Log.d(TAG, "getDaysUntil: $diff")
                return java.util.concurrent.TimeUnit.DAYS.convert(
                    diff,
                    java.util.concurrent.TimeUnit.MILLISECONDS
                ).toString()
            }
            catch(e:Exception){
                return "Не могу понять дату. Введите ее в формате день.месяц.год"
            }
        }

    }

}