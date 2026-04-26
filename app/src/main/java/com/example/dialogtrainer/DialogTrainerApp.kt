//DialogTrainerApp
package com.example.dialogtrainer

import android.app.Application
import com.example.dialogtrainer.core.AppDependencies

class DialogTrainerApp : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDependencies.init(this)
    }
}
