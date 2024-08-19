package com.example.loanapplication.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.loanapplication.R
import com.example.loanapplication.databinding.FragmentApplyLoanBinding
import com.example.loanapplication.ui.viewmodel.ApplyLoanViewModel
import com.example.loanapplication.util.diAppComponents

class ApplyLoanFragment : Fragment() {

    private lateinit var binding: FragmentApplyLoanBinding

    private lateinit var viewModel: ApplyLoanViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            diAppComponents.getViewModelFactory()
        )[ApplyLoanViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentApplyLoanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appyLoanButton.setOnClickListener {
            diAppComponents.getDialogManager().showCustomDialog(
                "Apply For Loan",
                message = "Enter your amount and duration",
                layout = R.layout.two_fild_coustom_layout,
                inflater = requireActivity().layoutInflater,
                context = requireContext(),
                onPositiveClick = { amount, duration ->
                    binding.loading.visibility = View.VISIBLE
                    viewModel.applyLoan(amount, duration)

                }
            )
        }

        viewModel.message.observe(requireActivity()) { success ->
            binding.loading.visibility = View.GONE
            diAppComponents.getDialogManager().showInfoDialog(
                message = success,
                context = this.requireContext()
            )
        }

    }

}