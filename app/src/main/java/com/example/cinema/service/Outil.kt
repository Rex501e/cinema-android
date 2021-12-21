package com.example.cinema.service

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Outil {

    fun DateToString(d: Date): String {
        val dateFormatpers: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        // on lance la conversion
        return dateFormatpers.format(d)
    }


    fun chaineTodate(s: String): Date {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        var date: Date =Date()
        try {
            date = formatter.parse(s) as Date
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return date;
    }
}