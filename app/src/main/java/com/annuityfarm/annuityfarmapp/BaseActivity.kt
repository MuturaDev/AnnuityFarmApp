package com.annuityfarm.annuityfarmapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import java.util.*


open class BaseActivity : AppCompatActivity() {

    val fAuthInstance: FirebaseAuth? by lazy { FirebaseAuth.getInstance() }

}