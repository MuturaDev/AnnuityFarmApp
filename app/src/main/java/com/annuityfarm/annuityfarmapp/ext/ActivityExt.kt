package com.annuityfarm.annuityfarmapp.ext;

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.annuityfarm.annuityfarmapp.Global
import com.annuityfarm.annuityfarmapp.R
import com.google.gson.Gson

// used for simple start activity without Intent parameters
fun Activity.goToActivity(context: Context, goto: Class<out Activity>, shouldFinishAfter: Boolean = false){
    startActivity(Intent(context, goto).let {
        it
    })
    if (shouldFinishAfter) finish()
}

fun Activity.goToFragment(goto: Int){
    findNavController(R.id.nav_host_fragment_content_login).navigate(goto)
}



fun <T> Activity.putShared(key: String?, value: T) {
    Global.editor(this).let {
        it.putString(key, Global.toGson(value))
        it.apply()
        it.commit()
    }
}

fun <T> Activity.getShared(key: String?, clazz: Class<T>?): T {
    return Gson().fromJson(Global.shared(this).getString(key, null), clazz)
}

fun Activity.clearShared(){
    Global.editor(this).let {
        it.clear()
        it.apply()
    }
}
