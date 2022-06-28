package com.namphuongso.acv.utils

class Validations {
    companion object{
        fun isEmailValid(email: String): Boolean {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        }
        fun isPhoneValid(phone: String): Boolean {
            return android.util.Patterns.PHONE.matcher(phone).matches()
        }
        fun isPhoneOrEmailValid(text: String): Boolean {
            return isPhoneValid(text) || isEmailValid(text)
        }


    }
}