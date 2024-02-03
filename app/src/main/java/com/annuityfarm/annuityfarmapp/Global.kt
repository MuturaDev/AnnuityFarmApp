package com.annuityfarm.annuityfarmapp

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Global {
   companion object {
       fun editor(context: Context): SharedPreferences.Editor{
           return context.getSharedPreferences(Constants.FRAGMENT_MESSAGE, Context.MODE_PRIVATE).edit()
       }

       fun shared(context: Context): SharedPreferences {
           return context.getSharedPreferences(Constants.FRAGMENT_MESSAGE, Context.MODE_PRIVATE)
       }

       fun toGson(any: Any?):String{
           return Gson().toJson(any)
       }
   }
}