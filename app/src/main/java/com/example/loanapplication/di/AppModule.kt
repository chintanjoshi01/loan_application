package com.example.loanapplication.di

import android.content.Context
import com.example.loanapplication.util.DialogManager
import com.example.loanapplication.util.InternetConnectivityCheck
import com.example.loanapplication.util.PermissionUtils
import com.example.loanapplication.util.SharedPreferencesUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideInternetConnectivityCheck(context: Context): InternetConnectivityCheck {
        return InternetConnectivityCheck(context)
    }

    @Provides
    @Singleton
    fun provideDialogManager(context: Context): DialogManager {
        return DialogManager(context)
    }

    @Provides
    @Singleton
    fun provideSharedPreferencesUtils(context: Context): SharedPreferencesUtils {
        return SharedPreferencesUtils(context)

    }

    @Provides
    @Singleton
    fun providePermissionsUtils(context: Context): PermissionUtils {
        return PermissionUtils(context)
    }

}