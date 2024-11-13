package com.quics.login.data


data class DirectionData(
    val lat: Double = 0.0,
    val long:Double = 0.0,
    val stringDirection:String="",
    var default:Boolean = false,
    val name:String="",
)