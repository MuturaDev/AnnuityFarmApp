package com.annuityfarm.annuityfarmapp.ext

import android.util.Log
import androidx.fragment.app.Fragment
import com.annuityfarm.annuityfarmapp.fragments.PensionProjectionCalculatorFragment
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.text.DecimalFormat


fun String.removeEmpty():String{
    return this.replace(" ", "").let { if(it.isEmpty()) "\"\"" else it }
}


fun String.removeHyphen():String{
    return this.replace("_", "-")
}

fun String.addHyphen():String{
    return this.replace("-", "_")
}

fun String.removeCurrencyFormat():String{
    return this.replace("Kshs.", "")
        .replace(",", "")
}

 fun String.convertToCurrencyWithDeciaml(fragment: Fragment): String {
    return if(this.isNotEmpty()) {
        val myFormatter = DecimalFormat("#,###.##")
        val currency: String = (fragment as PensionProjectionCalculatorFragment).currency
        this.let {
            currency + myFormatter.format(it.replace(currency, "").toDouble()).toString()
        }
    }
    else this
}

// TODO: 14/11/2021 https://mobikul.com/converting-string-md5-hashes-android/ 
fun String.toMD5():CharSequence{
    try {
        // Create MD5 Hash
        val digest: MessageDigest = MessageDigest.getInstance("MD5")
        digest.update(this.toByteArray())
        val messageDigest: ByteArray = digest.digest()

        // Create Hex String
        val hexString = StringBuffer()
        for (i in messageDigest.indices) hexString.append(
            Integer.toHexString(
                0xFF and messageDigest[i]
                    .toInt()
            )
        )
        return hexString.toString()
    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }
    return ""
}


fun String.compairStringMD5(hashed:CharSequence):Boolean{
    return this.toMD5() == hashed
}
