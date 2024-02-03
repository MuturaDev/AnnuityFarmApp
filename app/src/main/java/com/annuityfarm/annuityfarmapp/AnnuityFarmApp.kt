package com.annuityfarm.annuityfarmapp

import android.app.Application
import android.content.Context
import com.microblink.blinkinput.MicroblinkSDK
import java.io.File
import java.io.IOException


class AnnuityFarmApp : Application() {
    override fun onCreate() {
        super.onCreate()
        //val filePath =  getFileFromAssets(this, "com.microblink.input.mblic").absolutePath
        // TODO: 21/10/2021  License Expired.
       // MicroblinkSDK.setLicenseFile("license.key", this)
    }

//    @Throws(IOException::class)
//    fun getFileFromAssets(context: Context, fileName: String): File = File(context.cacheDir, fileName)
//        .also {
//            if (!it.exists()) {
//                it.outputStream().use { cache ->
//                    context.assets.open(fileName).use { inputStream ->
//                        inputStream.copyTo(cache)
//                    }
//                }
//            }
//        }
}