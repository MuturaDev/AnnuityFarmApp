package com.annuityfarm.annuityfarmapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.annuityfarm.annuityfarmapp.BaseFragment
import com.annuityfarm.annuityfarmapp.MainActivity
import com.annuityfarm.annuityfarmapp.R
import com.annuityfarm.annuityfarmapp.databinding.FragmentPasswordResetBinding
import com.annuityfarm.annuityfarmapp.ext.*

// TODO: 14/11/2021 UPDATE PASSWORD, https://blog.mindorks.com/firebase-login-and-authentication-android-tutorial 
class PasswordResetFragment : BaseFragment() {

    private var _binding: FragmentPasswordResetBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPasswordResetBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI(){

        binding.buttonFirst.setOnClickListener {
           // findNavController().navigate(R.id.action_PasswordResetFragment_to_LoginFragment)
        }

        binding.btnPasswordReset.setOnClickListener {
            if(binding.txtEmail.checkEmail()){
                passwordReset()
            }
        }
    }


    private fun passwordReset(){
        binding.apply {
            fAuthInstance?.sendPasswordResetEmail(txtEmail.getStringTrim())
                ?.addOnCompleteListener(requireActivity()){ task ->
                    if(task.isSuccessful) {
                        alert(
                            cancelable = false,
                            showIcon = R.drawable.ic_success,
                            title = "Confirmation",
                            titleColor = android.R.color.holo_green_dark,
                            message = "Reset link sent to your email",
                            messageColor = android.R.color.holo_green_dark
                        ) {
                        }.show()
                    }else {
                        alert(
                            cancelable = false,
                            showIcon = R.drawable.ic_red_close,
                            title = "An error occurred",
                            titleColor = R.color.colorError,
                            message = "Unable to send reset email ${task.exception?.message}",
                            messageColor = R.color.colorError
                        ) {
                        }.show()
                    }
                }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}