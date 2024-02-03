package com.annuityfarm.annuityfarmapp.ext

import android.app.Activity
import android.widget.Toast
import androidx.fragment.app.Fragment

// used for show a toast message in the UI Thread
fun Activity.toast(message: String) {
    runOnUiThread { Toast.makeText(this, message, Toast.LENGTH_SHORT).show() }
}

// used for show a toast message in the UI Thread
fun Fragment.toast(message: String) {
      Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
}