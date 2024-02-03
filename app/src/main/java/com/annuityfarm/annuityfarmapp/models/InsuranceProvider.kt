package com.annuityfarm.annuityfarmapp.models

class InsuranceProvider {
    var id:Int? = null
    var name:String? = null
    private var year:Int? = null
    var average:Double? = null
    var guaranteedRate:Double? = null
    var grossNet:String? = null

    override fun toString(): String {
        return "$name"
    }


}