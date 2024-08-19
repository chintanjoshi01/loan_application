package com.example.loanapplication.di

import android.app.Activity
import android.content.Context
import com.example.loanapplication.data.network.ApiService
import com.example.loanapplication.util.DialogManager
import com.example.loanapplication.util.InternetConnectivityCheck
import com.example.loanapplication.util.PermissionUtils
import com.example.loanapplication.util.SharedPreferencesUtils
import dagger.BindsInstance
import dagger.Component
import retrofit2.Retrofit
import javax.inject.Singleton


@Singleton
@Component(
    modules = [ViewModelModules::class,
        APIModule::class, AppModule::class]
)
interface AppComponents {

    fun inject(activity: Activity)

    fun getViewModelFactory(): ViewModelFactory

    fun getApiServices(): ApiService

    fun getRetrofit(): Retrofit

    fun getInternetConnectivityCheck(): InternetConnectivityCheck

    fun getSharedPreferencesUtils(): SharedPreferencesUtils

    fun getPermissionsUtils(): PermissionUtils

fun getDialogManager(): DialogManager

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponents
    }

}