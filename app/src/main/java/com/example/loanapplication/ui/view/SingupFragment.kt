package com.example.loanapplication.ui.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.loanapplication.R
import com.example.loanapplication.databinding.FragmentSingupBinding
import com.example.loanapplication.ui.viewmodel.AuthViewModel
import com.example.loanapplication.util.diAppComponents

class SingupFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentSingupBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        diAppComponents.getPermissionsUtils().grantPermission(requireContext())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            diAppComponents.getViewModelFactory()
        )[AuthViewModel::class.java]


        binding.signUpButton.isEnabled = false
        viewModel.loginFormState.observe(requireActivity(), Observer { loginState ->
            val loginState = loginState ?: return@Observer
            binding.signUpButton.isEnabled = loginState.isDataValid
            binding.usernameLayout.error = loginState.emailIdError?.let { getString(it) }
            binding.passwordLayout.error = loginState.passwordError?.let { getString(it) }
            binding.mobileLayout.error = loginState.mobileError?.let { getString(it) }
            binding.confirmLayout.error = loginState.confirmPasswordError?.let { getString(it) }
        })

        binding.username.afterTextChanged { updateLoginData("username") }
        binding.password.afterTextChanged { updateLoginData("password") }
        binding.confirmPassword.afterTextChanged { updateLoginData("confirmPassword") }
        binding.mobile.afterTextChanged { updateLoginData("mobile") }


        binding.signUpButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.singUp(
                binding.username.text.toString(),
                binding.password.text.toString(),
                binding.mobile.text.toString()
            )
        }

        viewModel.message.observe(requireActivity()) { success ->
            binding.loading.visibility = View.GONE
            diAppComponents.getDialogManager().showInfoDialog(
                message = success,
                context = this.requireContext(),
                onPositiveClick = {
                    findNavController().navigate(R.id.action_signupFragment_to_loginFragment)

                }
            )
        }

    }

    private fun updateLoginData(field: String) {
        when (field) {
            "username" -> viewModel.loginDataChanged(
                username = binding.username.text.toString(),
                password = null,
                confirmPassword = null,
                mobile = null
            )

            "password" -> viewModel.loginDataChanged(
                username = null,
                password = binding.password.text.toString(),
                confirmPassword = binding.confirmPassword.text.toString(),
                mobile = null
            )

            "confirmPassword" -> viewModel.loginDataChanged(
                username = null,
                password = binding.password.text.toString(),
                confirmPassword = binding.confirmPassword.text.toString(),
                mobile = null
            )

            "mobile" -> viewModel.loginDataChanged(
                username = null,
                password = null,
                confirmPassword = null,
                mobile = binding.mobile.text.toString()
            )
        }
    }


    private fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
        this.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                afterTextChanged.invoke(editable.toString())
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        })
    }

}