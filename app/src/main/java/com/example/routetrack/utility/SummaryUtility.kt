package com.example.routetrack.utility

import android.util.Log
import java.util.Calendar

object SummaryUtility {

    val currentMonth = Calendar.getInstance().get(Calendar.MONTH)

    fun getMonthStart(month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        Log.d("SummaryUtility", calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }

    fun getMonthEnd(month: Int): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH))
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        calendar.set(Calendar.MILLISECOND, 999)
        Log.d("SummaryUtility", calendar.timeInMillis.toString())
        return calendar.timeInMillis
    }

}