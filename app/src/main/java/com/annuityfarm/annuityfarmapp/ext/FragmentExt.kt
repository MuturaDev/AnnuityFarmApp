package com.annuityfarm.annuityfarmapp.ext

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.annuityfarm.annuityfarmapp.Constants.Companion.FRAGMENT_MESSAGE
import com.annuityfarm.annuityfarmapp.Global
import com.annuityfarm.annuityfarmapp.R
import com.google.gson.Gson


//https://stackoverflow.com/questions/7145606/how-do-you-save-store-objects-in-sharedpreferences-on-android



fun Fragment.goToFragment(goto: Int){
    findNavController().navigate(goto)
}

fun <T> Fragment.putShared(key: String?, value: T) {
    Global.editor(requireActivity()).let {
        it.putString(key, Global.toGson(value))
        it.apply()
        it.commit()
    }
}

fun <T> Fragment.getShared(key: String?, clazz: Class<T>?): T {
    return Gson().fromJson(Global.shared(requireActivity()).getString(key, null), clazz)
}

fun Fragment.clearShared(){
    Global.editor(requireActivity()).let {
        it.clear()
        it.apply()
    }
}


fun Fragment.getResourceString(key: Int, isArray: Boolean):String{
    val index = 0
    return if(isArray) resources.getStringArray(key)[index].toString() else resources.getString(key)
}



