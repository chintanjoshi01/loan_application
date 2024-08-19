package com.example.loanapplication

import android.app.Application
import com.example.loanapplication.di.DaggerAppComponents
import com.example.loanapplication.util.diAppComponents

class MyApp : Application() {


    override fun onCreate() {
        super.onCreate()
        diAppComponents = DaggerAppComponents.factory().create(this)
    }


}