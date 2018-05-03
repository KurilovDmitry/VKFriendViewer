package com.example.dmitrykurilov.vkfriendviewer

import android.annotation.SuppressLint
import android.content.Context
import com.activeandroid.app.Application

class VKFriendViewerApplication : Application() {

        companion object {
            @SuppressLint("StaticFieldLeak")
            lateinit var context: Context
        }

        override fun onCreate() {
            super.onCreate()

            context = this
    }
}