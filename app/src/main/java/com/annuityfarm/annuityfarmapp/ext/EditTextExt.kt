package com.annuityfarm.annuityfarmapp.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.annuityfarm.annuityfarmapp.isValidEmail


/**
 * Created by Chathura Wijesinghe on 2019-06-12.
 * cdanasiri@gmail.com
 */

/**
 * This extension supports EditText validation such as emails, maximum and minimum length
 * */
//https://snippets.cdan.me/kotlin-extensions/edittext-validation-extensions
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
            afterTextChanged.invoke(s.toString().trim())
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
    })
}

/**
 * You can validate the EditText like
 *
 * et_email.validate("Valid email address required"){ s -> s.isValidEmail() }
 *
 * et_email.validate("Minimum length is 6"){ s-> s.length>=6 }
 *
 * */
fun EditText.validate(message: String, validator: (String) -> Boolean) : Boolean{
    this.afterTextChanged {
        this.error = if (validator(it)) null else message
    }
    this.error = if (validator(this.getString())) null else message

    return validator(this.getString())
}


fun EditText.checkEmpty(field:String):Boolean{
   return validate( "$field is required", validator = {s-> s.isNotEmpty() })
}

fun EditText.checkEmail():Boolean{
    return validate("Valid email address required", validator = {s -> s.isValidEmail()})
}

/**
 * @return String value of the EditTextView
 * */
fun EditText.getString(): String{
    return this.text.toString()
}

/**
 * @return Trimmed String value of the EditTextView
 * */
fun EditText.getStringTrim(): String{
    return this.getString().trim()
}