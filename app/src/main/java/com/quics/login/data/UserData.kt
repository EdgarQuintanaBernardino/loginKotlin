package com.quics.login.data

import java.util.Date

data class User(
    var nombre: String="",
    var direcciones: List<DirectionData> = emptyList(),
    var telefonos:List<String> = emptyList(),
    var email:String ="",
    var fnacimiento:Date = Date(),
    var foto:String ="",
    var uid:String="",
    var metodos_pago:List<Any> = emptyList(),
)
fun User.toMap():Map<String,Any>{
    return mapOf(
        "nombre" to nombre,
        "direcciones" to direcciones,
        "telefonos" to telefonos,
        "email" to email,
        "fnacimiento" to fnacimiento,
        "foto" to foto,
        "uid" to uid,
        "metodos_pago" to metodos_pago
    )
}