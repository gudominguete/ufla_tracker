package com.flex.flextracker.enum

import com.flex.flextracker.utils.UuidUtils
import java.util.*



enum class UuidBle(val uuid: UUID) {
    HEART_RATE_SERVICE_UUID( UuidUtils.convertFromInteger(0x180D)),
    HEART_RATE_MEASUREMENT_CHAR_UUID(UuidUtils.convertFromInteger(0x2A37)),
    HEART_RATE_CONTROL_POINT_CHAR_UUID(UuidUtils.convertFromInteger(0x2A39))
}