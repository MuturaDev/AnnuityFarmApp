package com.annuityfarm.annuityfarmapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.annuityfarm.annuityfarmapp.databinding.FragmentRegisterLayoutBinding
import com.annuityfarm.annuityfarmapp.ext.*
import com.annuityfarm.annuityfarmapp.objects.FirebaseUtils.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

import android.content.Intent

import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.models.DataController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.collection.LLRBNode


/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterLayoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()

        registerLastAppLaunch()

    }

    private fun registerLastAppLaunch(){

    }

    private var message:String? = null


    private fun initUI(){

        arguments?.let {
            RegisterFragmentArgs.fromBundle(it).message.let {
                when(it){
                    "Edit" -> {
                        binding.btnlogout.apply {
                            this.show()
                            setOnClickListener {
                                fAuthInstance?.signOut()
                                activity?.let {
                                    it.goToActivity(it, goto = LoginActivity::class.java, shouldFinishAfter = true)
                                }
                            }
                        }
                        binding.txtHeader.gone()
                        binding.layoutAlreadyHaveAccount.gone()
                        binding.include.layoutSignwithGoogle.gone()
                        binding.btnSignUp.text = "Edit"
                        binding.txtSmallHeader.text = "Your account profile"
                    }
                }

                message = it
            }
        }


        binding.buttonSecond.setOnClickListener {
            goToFragment(R.id.action_RegisterFragment_to_LoginFragment)
        }

        binding.btnSignUp.setOnClickListener {
            if(binding.txtEmail.checkEmail()
                && binding.txtPassword.checkEmpty("Password")
            ){
                message?.let {
                    editProfile()
                    return@setOnClickListener
                }
                signUp()
            }

        }
    }


    private fun editProfile(){

    }


    private fun signUp(){
        hideKeyboard()
        showLoading()
        binding.apply {
            fAuthInstance?.createUserWithEmailAndPassword(txtEmail.getStringTrim(), txtPassword.getStringTrim().toMD5().toString())
                ?.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        completeSignUp()
                    }else{
                        closeLoading()
                        alert(
                            cancelable = true,
                            showIcon = R.drawable.ic_red_close,
                            title = "An error occurred",
                            titleColor = R.color.colorError,
                            message = (task.exception as FirebaseAuthException).errorCode.getTaskExcMessage(),
                            messageColor = R.color.colorError
                        ) {
                        }.show()
                    }
                }
        }
       // activity?.goToActivity(requireActivity(), MainActivity::class.java, true)
    }

    private fun completeSignUp(){
        if(binding.txtFirstName.checkEmpty("First Name")
            && binding.txtLastName.checkEmpty("Last Name")
            && binding.txtMobileNumber.checkEmpty("Mobile Number")){

                binding.apply {
                    val user = mapOf(
                        "firstName" to txtFirstName.getStringTrim(),
                        "lastName" to txtLastName.getStringTrim(),
                        "mobile" to txtMobileNumber.getStringTrim(),
                        "email" to txtEmail.getStringTrim(),
                        "password" to txtPassword.getStringTrim().toMD5()
                    )

                    dataC.updateUserProfile(user, onSuccess = {
                        closeLoading()
                        findNavController().navigate(R.id.action_RegisterFragment_to_LoginFragment)
                    },
                    onFailure = {
                        closeLoading()
                        alert(
                            cancelable = true,
                            showIcon = R.drawable.ic_red_close,
                            title = "An error occurred",
                            titleColor = R.color.colorError,
                            message = (it as FirebaseAuthException).errorCode.getTaskExcMessage(),
                            messageColor = R.color.colorError
                        ) {
                        }.show()
                    })


                }


        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}