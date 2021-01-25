package hr.kacan.trznica.utils

import android.content.Context
import android.util.TypedValue
import kotlin.math.roundToInt

object Tools {
    fun dpToPx(c: Context, dp: Int): Int {
        val r = c.resources
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), r.displayMetrics).roundToInt()
    }
}