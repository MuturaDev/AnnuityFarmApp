package com.annuityfarm.annuityfarmapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.annuityfarm.annuityfarmapp.*
import com.annuityfarm.annuityfarmapp.databinding.FragmentLoginLayoutBinding

import com.annuityfarm.annuityfarmapp.ext.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuthException


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : BaseFragment() {

    private var _binding: FragmentLoginLayoutBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginLayoutBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI(){
        binding.btnForgetPassword.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_PasswordResetFragment)
        }

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        binding.btnLogin.setOnClickListener {
            if(binding.txtEmail.checkEmail() &&
                binding.txtPassword.checkEmpty("Password")){
                signIn()
            }
        }

        binding.root.findViewById<LinearLayout>(R.id.btnSignWithGoogle).setOnClickListener {
            signInGoogle()
        }
    }

    // TODO: 14/11/2021 https://heartbeat.comet.ml/implementing-social-login-and-user-profile-management-in-android-with-firebase-de1cbd982d44
    private fun signInGoogle(){
        // we are using Google, Email-Password, and Phone Number based authentication
//        val providers = listOf(
//            fAuthInstance.IdpConfig.GoogleBuilder().build(),
//            AuthUI.IdpConfig.EmailBuilder().build(),
//            AuthUI.IdpConfig.PhoneBuilder().build())
//
//        val authIntent = AuthUI.getInstance().createSignInIntentBuilder()
//            // set a custom logo to be shown on the login screen
//            .setLogo(R.mipmap.ic_launcher)
//            // set the login screen's theme
//            .setTheme(R.style.AppThemeNoActionbar)
//            // define the providers that will be used
//            .setAvailableProviders(providers)
//            // disable smart lock that automatically logs in a previously logged in user
//            .setIsSmartLockEnabled(false)
//            // set the terms of service and private policy URL for your app
//            .setTosAndPrivacyPolicyUrls("example.termsofservice.com", "example.privatepolicy.com")
//            .build()
//
//        startActivity(authIntent)
    }


    private fun signIn(){
        hideKeyboard()
        showLoading()

        binding.apply {
            val password = txtPassword.getStringTrim().toMD5().toString()

            dataC.sigInUser(txtEmail.getStringTrim(),password,
                    onSuccess = {
                        closeLoading()
                        activity?.goToActivity(requireActivity(), MainActivity::class.java, true)
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
                    }
                )
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}