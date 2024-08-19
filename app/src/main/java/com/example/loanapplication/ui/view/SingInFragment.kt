package com.example.loanapplication.ui.view

import android.content.Intent
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
import com.example.loanapplication.databinding.FragmentLoginBinding
import com.example.loanapplication.ui.viewmodel.AuthViewModel
import com.example.loanapplication.util.PREFERENCES_DEFULT_USERNAME
import com.example.loanapplication.util.PREFERENCES_KEY_MOBILE
import com.example.loanapplication.util.PREFERENCES_KEY_USERNAME
import com.example.loanapplication.util.diAppComponents

class SingInFragment : Fragment() {


    private lateinit var viewModel: AuthViewModel
    private lateinit var binding: FragmentLoginBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferencesUtils = diAppComponents.getSharedPreferencesUtils()
        val username = sharedPreferencesUtils.getStringShard(
            PREFERENCES_KEY_USERNAME,
            PREFERENCES_DEFULT_USERNAME
        )
        val mobile =
            sharedPreferencesUtils.getStringShard(PREFERENCES_KEY_MOBILE, PREFERENCES_KEY_MOBILE)

        if (!username.equals(PREFERENCES_DEFULT_USERNAME) && !mobile.equals(PREFERENCES_KEY_MOBILE)) {
            val intent = Intent(requireContext(), HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            requireActivity().finish()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            diAppComponents.getViewModelFactory()
        )[AuthViewModel::class.java]

        binding.signInButton.setOnClickListener {
            binding.loading.visibility = View.VISIBLE
            viewModel.singIn(
                binding.username.text.toString(),
                binding.password.text.toString(),
                binding.mobile.text.toString()
            )
        }


        viewModel.loginFormState.observe(requireActivity(), Observer { loginState ->
            val loginState = loginState ?: return@Observer
            binding.signInButton.isEnabled = loginState.isDataValid
            binding.usernameLayout.error = loginState.emailIdError?.let { getString(it) }
            binding.passwordLayout.error = loginState.passwordError?.let { getString(it) }
            binding.mobileLayout.error = loginState.mobileError?.let { getString(it) }
        })

        binding.username.afterTextChanged { updateLoginData("username") }
        binding.password.afterTextChanged { updateLoginData("password") }
        binding.mobile.afterTextChanged { updateLoginData("mobile") }

        binding.signUpButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }

        viewModel.message.observe(requireActivity()) { success ->
            binding.loading.visibility = View.GONE
            diAppComponents.getDialogManager().showInfoDialog(
                message = success,
                context = this.requireContext(),
                onPositiveClick = {
                    if (viewModel.isSingin.value == true) {
                        val intent = Intent(requireContext(), HomeActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(intent)
                        requireActivity().finish()
                    }
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
                confirmPassword = null,
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