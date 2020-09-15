package com.ufla.gustavo.uflatracker.ui.atividade

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter
import java.text.DecimalFormat

class XAxisFormatter : ValueFormatter() {
    private val format = DecimalFormat("###s")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return format.format(value)
    }
}