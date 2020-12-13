package com.flex.flextracker.utils

import java.util.*

class UuidUtils {
    companion object {
        fun convertFromInteger(i : Int): UUID {
            val MSB = 0x0000000000001000L
            val LSB = -0x7fffff7fa064cb05L
            val value = i and -0x1
            return UUID(MSB or ((value shl 32).toLong()), LSB)
        }
    }
}