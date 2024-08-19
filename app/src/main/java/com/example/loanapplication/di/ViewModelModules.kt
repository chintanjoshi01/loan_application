package com.example.loanapplication.di

import androidx.lifecycle.ViewModel
import com.example.loanapplication.ui.viewmodel.ApplyLoanViewModel
import com.example.loanapplication.ui.viewmodel.AuthViewModel
import com.example.loanapplication.ui.viewmodel.LoanHistoryViewModel
import com.example.loanapplication.ui.viewmodel.ProfileViewModel
import dagger.Binds
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap


@dagger.Module
abstract class ViewModelModules {


    @Binds
    @ClassKey(AuthViewModel::class)
    @IntoMap
    abstract fun singUpViewModel(authViewModel: AuthViewModel): ViewModel


    @Binds
    @ClassKey(ApplyLoanViewModel::class)
    @IntoMap
    abstract fun applyLoanViewModel(applyLoanViewModel: ApplyLoanViewModel): ViewModel

    @Binds
    @ClassKey(LoanHistoryViewModel::class)
    @IntoMap
    abstract fun loanHistoryViewModel(loanHistoryViewModel: LoanHistoryViewModel): ViewModel

    @Binds
    @ClassKey(ProfileViewModel::class)
    @IntoMap
    abstract fun profileViewModel(profileViewModel: ProfileViewModel): ViewModel


}