package com.annuityfarm.annuityfarmapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View

import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.annuityfarm.annuityfarmapp.databinding.ActivitySplashBinding
import com.annuityfarm.annuityfarmapp.ext.goToActivity
import com.annuityfarm.annuityfarmapp.models.DataController
import com.google.firebase.auth.FirebaseAuth


class SplashActivity : BaseActivity(), View.OnClickListener {


    override fun onClick(v: View?) {
        when(v?.id)
        {

        }
    }

    private lateinit var binding: ActivitySplashBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

    }


    private fun initUI(){
        val anim = AnimationUtils.loadAnimation(this@SplashActivity, R.anim.slide_down)
        binding.image.startAnimation(anim)

        Handler(Looper.getMainLooper()).postDelayed({
            binding.image.startAnimation(AnimationUtils.loadAnimation(this@SplashActivity, R.anim.transperency))
        }, 2500)

        Handler(Looper.getMainLooper()).postDelayed({
            DataController(this@SplashActivity).getCurrentUser(
                onSuccess = {
                    if(it == null)
                        goToActivity(this, LoginActivity::class.java, true)
                    else
                        goToActivity(this, MainActivity::class.java, true)
                }
            )
        }, 8000)

    }
}