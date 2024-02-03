package com.annuityfarm.annuityfarmapp.models

import android.app.Activity
import com.annuityfarm.annuityfarmapp.LoginActivity
import com.annuityfarm.annuityfarmapp.MainActivity
import com.annuityfarm.annuityfarmapp.ext.ModelContext
import com.annuityfarm.annuityfarmapp.ext.goToActivity
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import java.util.*


class DataController(act: Activity) {

    private val fDB: FirebaseFirestore? by lazy { FirebaseFirestore.getInstance() }
    private val fAuthInstance: FirebaseAuth? by lazy { FirebaseAuth.getInstance() }
    private var fCurrentUser: FirebaseUser? = null
    private var activity:Activity = act

    init {
        FirebaseAuth.AuthStateListener { firebaseAuth ->
            fCurrentUser = firebaseAuth.currentUser
        }
    }

    fun updateUserProfile(user:Map<String,Any>,onSuccess:(DocumentReference) -> Unit, onFailure:(Exception) -> Unit){
        fDB?.collection(ModelContext.users.s())
            ?.add(user)
            ?.addOnSuccessListener { documentReference ->
               onSuccess.invoke(documentReference)
            }
            ?.addOnFailureListener { error ->
                onFailure.invoke(error)
            }
    }

    fun sigInUser(email:String, password:String, onSuccess:(Task<AuthResult>) -> Unit, onFailure:(Exception) -> Unit){
        fAuthInstance?.signInWithEmailAndPassword(email,password)
            ?.addOnCompleteListener(activity){ task ->
                if(task.isSuccessful) {
                    onSuccess.invoke(task)
                }else {
                    task.exception?.let { onFailure.invoke(it) }
                }
            }
    }

    fun getUserProfile(onSuccess:(Map<String,Any>) -> Unit, onFailure:(Exception) -> Unit){
        fDB?.collection(ModelContext.users.s())
            ?.document(fCurrentUser!!.uid)
            ?.get()
            ?.addOnSuccessListener { document ->
                if (document != null) {
                    //Log.d(TAG, "DocumentSnapshot data: ${document.data}")
                    document.data?.let { onSuccess.invoke(it) }
                }
            }
            ?.addOnFailureListener { exception ->
                onFailure.invoke(exception)
            }
    }

    fun getCurrentUser(onSuccess:(FirebaseUser?) -> Unit){
         val authStateListener =
            FirebaseAuth.AuthStateListener { firebaseAuth ->
                val firebaseUser = firebaseAuth.currentUser
                if (firebaseUser == null) {
                    onSuccess.invoke(firebaseUser)
                }
                if (firebaseUser != null) {
                    onSuccess.invoke(firebaseUser)
                }
            }
        fAuthInstance?.addAuthStateListener(authStateListener)
    }
}