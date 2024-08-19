package com.example.loanapplication.ui.view

import LoanHistoryAdeptor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.loanapplication.databinding.FragmentLoanHistoryBinding
import com.example.loanapplication.ui.viewmodel.LoanHistoryViewModel
import com.example.loanapplication.util.diAppComponents

class LoanHistoryFragment : Fragment() {


    private lateinit var viewModel: LoanHistoryViewModel
    private lateinit var binding: FragmentLoanHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            diAppComponents.getViewModelFactory()
        )[LoanHistoryViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoanHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = LoanHistoryAdeptor()
        binding.rvLoanHistory.adapter = adapter
        binding.rvLoanHistory.layoutManager = LinearLayoutManager(requireContext())
        viewModel.loanList.observe(requireActivity()) {
            if (it.loans.isEmpty() || it == null) {
                binding.tvNoLoanHistory.visibility = View.VISIBLE
            } else {
                binding.rvLoanHistory.visibility = View.VISIBLE
                binding.tvNoLoanHistory.visibility = View.GONE
                adapter.submitList(it.loans)
            }
        }
        viewModel.errorMessage.observe(requireActivity()) {
            binding.tvNoLoanHistory.visibility = View.VISIBLE
            binding.rvLoanHistory.visibility = View.GONE
            binding.tvNoLoanHistory.text = it
        }

    }


}