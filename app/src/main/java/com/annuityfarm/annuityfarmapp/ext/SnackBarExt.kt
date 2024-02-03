package com.annuityfarm.annuityfarmapp.ext;

import android.graphics.Color
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.annuityfarm.annuityfarmapp.R
import com.google.android.material.snackbar.Snackbar


inline fun View.snackWithAction(message: String, length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

 fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) {
    val snack = Snackbar.make(this, message, length)
     snack.view.setBackgroundColor(ContextCompat.getColor(context, R.color.red))
     snack.show()






}

fun Snackbar.action(action: String, color: Int? = null, listener: (View) -> Unit) {
    setAction(action, listener)
    color?.let { setActionTextColor(color) }
}
