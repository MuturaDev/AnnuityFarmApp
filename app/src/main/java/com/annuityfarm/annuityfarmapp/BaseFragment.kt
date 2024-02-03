package com.annuityfarm.annuityfarmapp

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.annuityfarm.annuityfarmapp.ext.goToActivity
import com.annuityfarm.annuityfarmapp.models.DataController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.roger.catloadinglibrary.CatLoadingView

//
//import androidx.fragment.app.Fragment
//
open class BaseFragment : Fragment(){

   open val sanlamWebsiteUrl: String = "https://ibuildglobal.atlassian.net/wiki/spaces/IHD/pages/1678966785/Customer+FAQS#Getting-started%3A"

    val fAuthInstance: FirebaseAuth? by lazy { FirebaseAuth.getInstance() }
    var fCurrentUser:FirebaseUser? = null
    val fDB:FirebaseFirestore? by lazy { FirebaseFirestore.getInstance() }
    var dataC = DataController(requireActivity())

//    private val loadingView:CatLoadingView? by lazy { CatLoadingView() }
//
//    fun showLoading(){
//        loadingView?.show(requireActivity().supportFragmentManager, "")
//    }
//
//    fun closeLoading(){
//        loadingView?.dismiss()
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            fCurrentUser = firebaseAuth.currentUser
        }
        super.onViewCreated(view, savedInstanceState)
    }

}