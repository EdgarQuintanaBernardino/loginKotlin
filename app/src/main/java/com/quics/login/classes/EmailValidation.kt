package com.login.classes

class EmailValidation {

        companion object{
            private val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$".toRegex()
            fun isEmail(email:String): Boolean {
                return emailRegex.matches(email);
            }
        }



}