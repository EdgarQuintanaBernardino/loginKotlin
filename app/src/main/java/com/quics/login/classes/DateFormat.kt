package com.quics.login.classes

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DateFormat {
     lateinit var fecha: Calendar;
        constructor(fechaDate: Date){
            var calendar=Calendar.getInstance();
            calendar.time=fechaDate;
            fecha=calendar;

        }
    fun getYear(): Int {
        return fecha.get(Calendar.YEAR);
    }
    fun getMonth(): Int {
        return fecha.get(Calendar.MONTH);
    }
    fun getDay(): Int {
        return fecha.get(Calendar.DAY_OF_MONTH);
    }
//    fun convertirFecha(fechaFirebase:String): String? {
//        // Formato de la fecha de entrada
//        val formatoEntrada = SimpleDateFormat("dd 'de' MMMM 'de' yyyy, hh:mm:ss a z", Locale("es", "MX"))
//
//        // Parseamos la fecha de entrada
//        val fecha = formatoEntrada.parse(fechaFirebase)
//
//        // Formato de la fecha de salida (yyyy-MM-dd)
//        val formatoSalida = SimpleDateFormat("yyyy-MM-dd")
//
//        // Convertimos la fecha al nuevo formato
//        return formatoSalida.format(fecha)
//    }
}