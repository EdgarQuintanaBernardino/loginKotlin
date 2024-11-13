package com.quics.login.models

data class AddressItemModel(

    val stringDirection:String,
    val name:String,
    val lat : Double,
    val long : Double,
    val default : Boolean) {
    var isSelected: Boolean = false
}

