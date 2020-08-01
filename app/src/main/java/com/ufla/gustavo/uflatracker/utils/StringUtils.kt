package com.ufla.gustavo.uflatracker.utils

class StringUtils {
    companion object {
        fun formatCpf(cpf : String): String{
            return cpf.substring(0,3)+"."+cpf.substring(3,6)+"."+cpf.substring(6,9)+"-"+cpf.substring(9,11)
        }
    }
}