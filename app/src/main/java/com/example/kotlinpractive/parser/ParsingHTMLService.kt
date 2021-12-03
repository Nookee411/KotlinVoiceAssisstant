package ru.example.voiceassistentkotlin.parser

import android.util.Log
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element


class ParsingHTMLService {
    companion object{
        val URL = "http://mirkosmosa.ru/holiday/2020"
        fun getHoliday(question:String):List<Element>{
            val body = Jsoup.connect(URL).get().body()
            val date = getDate(question)
            return body.select("div.month_cel_date").filter{element -> element.child(0).text() === date }
        }

        private fun getDate(question:String): String {
            var date = question.substring(8)
            return "ads"
        }
    }
}