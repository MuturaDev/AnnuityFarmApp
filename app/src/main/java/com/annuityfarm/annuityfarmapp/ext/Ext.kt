package com.annuityfarm.annuityfarmapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.RelativeLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.roger.catloadinglibrary.CatLoadingView
import java.util.regex.Pattern


fun Context.toast(message: CharSequence, duration: Int = Toast.LENGTH_LONG) {
    Toast.makeText(this, message, duration).show()
}

val EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"(),:;<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$"

// used for validate if the current String is an email
fun String.isValidEmail(): Boolean {
    val pattern = Pattern.compile(EMAIL_PATTERN)
    return pattern.matcher(this).matches()
}

fun View.show() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.hide() { visibility = View.INVISIBLE }

fun String.upperCaseFirstLetter(): String {
    return this.substring(0, 1).toUpperCase().plus(this.substring(1))
}

//fun View.hideKeyboard() {
//    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//    imm.hideSoftInputFromWindow(windowToken, 0)
//}

/**
 * Extension method to show a keyboard for View.
 */
fun View.showKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    this.requestFocus()
    imm.showSoftInput(this, 0)
}

/**
 * Try to hide the keyboard and returns whether it worked
 * Please note that this is a Context extension.
 * https://stackoverflow.com/questions/1109022/close-hide-the-android-soft-keyboard
 */
fun View.hideKeyboard(): Boolean {
    try {
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        return inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
    } catch (ignored: RuntimeException) { }
    return false
}

/**
 * Show a snackbar with [message]
 */
//fun View.snack(message: String, length: Int = Snackbar.LENGTH_LONG) = snack(message, length) {}

/**
 * Show a snackbar with [message], execute [f] and show it
 */
inline fun View.snack(message: String, @Snackbar.Duration length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, message, length)
    snack.f()
    snack.show()
}

/**
 * Show a snackbar with [messageRes], execute [f] and show it
 * buttonSubmit.snack(R.string.name_submitted, Snackbar.LENGTH_LONG, {
    //TODO: Snackbar is now shown. Make some food :)
    })
 */
inline fun View.snack(@StringRes messageRes: Int, @SuppressLint("Range") @Snackbar.Duration length: Int = Snackbar.LENGTH_LONG, f: Snackbar.() -> Unit) {
    val snack = Snackbar.make(this, messageRes, length)
    snack.f()
    snack.show()
}

//MORE
//https://medium.com/@dev.malwinder/kotlin-extensions-for-android-view-96ee5cafa921
//http://kotlinextensions.com/

fun View.expand() {
    val matchParentMeasureSpec =
        View.MeasureSpec.makeMeasureSpec((this.parent as View).width, View.MeasureSpec.EXACTLY)
    val wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(matchParentMeasureSpec, wrapContentMeasureSpec)
    val targetHeight = measuredHeight

    // Older versions of android (pre API 21) cancel animations for views with a height of 0.
    layoutParams.height = 1
    visibility = View.VISIBLE
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            layoutParams.height =
                if (interpolatedTime == 1f) RelativeLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
            requestLayout()
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Expansion speed of 1dp/ms
    a.duration = ((targetHeight / context.resources.displayMetrics.density.toLong()).toInt()).toLong() + 500
    startAnimation(a)
}

 fun View.collapse() {
    val initialHeight = this.measuredHeight
    val a: Animation = object : Animation() {
        override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
            if (interpolatedTime == 1f) {
                visibility = View.GONE
            } else {
                layoutParams.height = initialHeight - (initialHeight * interpolatedTime).toInt()
                requestLayout()
            }
        }

        override fun willChangeBounds(): Boolean {
            return true
        }
    }

    // Collapse speed of 1dp/ms
    a.duration = ((initialHeight / context.resources.displayMetrics.density.toLong()).toInt()).toLong() + 500
    startAnimation(a)
}


val Context.isConnected: Boolean
    get() {
        val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M -> {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    else -> false
                }
            }
            else -> {
                // Use depreciated methods only on older devices
                val nwInfo = connectivityManager.activeNetworkInfo ?: return false
                nwInfo.isConnected
            }
        }
    }


fun View.enable() {
    isEnabled = true
}

fun View.disable() {
    isEnabled = false
}

fun View.showOrHide(show: Boolean) = if (show) show() else hide()


// Spinners
fun Spinner.onItemSelected(cb: (index: Int) -> Unit) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onNothingSelected(parent: AdapterView<*>?) = Unit

        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) = cb(position)
    }
}


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

private val loadingView: CatLoadingView? by lazy { CatLoadingView() }
fun Fragment.showLoading(){
//    loadingView?.apply {
//        // if(isAdded && activity != null) {
//        //setBackgroundColor(R.color.violet_200)//Color.parseColor("#000000")
//        show(requireActivity().supportFragmentManager, "")
//        //}
//    }
    loadingView?.setBackgroundColor(R.color.violet_200)//Color.parseColor("#000000")
    loadingView?.show(requireActivity().supportFragmentManager, "")
}

fun Fragment.closeLoading(){
    loadingView?.dismiss()
}


fun String.getTaskExcMessage():String{
    val errorCode = this
   return when (errorCode) {
        "ERROR_INVALID_CUSTOM_TOKEN" -> 
            "The custom token format is incorrect. Please check the documentation."
        "ERROR_CUSTOM_TOKEN_MISMATCH" -> 
            "The custom token corresponds to a different audience."
        "ERROR_INVALID_CREDENTIAL" -> 
            "The supplied auth credential is malformed or has expired."
        "ERROR_INVALID_EMAIL" -> "The email address is badly formatted."
        "ERROR_WRONG_PASSWORD" ->
                "The password is invalid or the user does not have a password."
        "ERROR_USER_MISMATCH" -> 
            "The supplied credentials do not correspond to the previously signed in user."
        "ERROR_REQUIRES_RECENT_LOGIN" -> 
            "This operation is sensitive and requires recent authentication. Log in again before retrying this request."
        "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL" -> 
            "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address."
        "ERROR_EMAIL_ALREADY_IN_USE" ->
                "The email address is already in use by another account."
        "ERROR_CREDENTIAL_ALREADY_IN_USE" -> 
            "This credential is already associated with a different user account."
        "ERROR_USER_DISABLED" -> 
            "The user account has been disabled by an administrator."
        "ERROR_USER_TOKEN_EXPIRED" -> 
            "The user\\'s credential is no longer valid. The user must sign in again."
        "ERROR_USER_NOT_FOUND" -> 
            "There is no user record corresponding to this identifier. The user may have been deleted."
        "ERROR_INVALID_USER_TOKEN" -> 
            "The user\\'s credential is no longer valid. The user must sign in again."
        "ERROR_OPERATION_NOT_ALLOWED" -> 
            "This operation is not allowed. You must enable this service in the console."
        "ERROR_WEAK_PASSWORD" ->
           "The password is invalid it must 6 characters at least"
       else -> ""
   }
}