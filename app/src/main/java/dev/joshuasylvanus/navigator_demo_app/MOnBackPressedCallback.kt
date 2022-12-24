package dev.joshuasylvanus.navigator_demo_app

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

open class MOnBackPressedCallback(private var activity: Activity)
    : OnBackPressedCallback(true) {
    override fun handleOnBackPressed() {
        if(!ActivityUtils.isLastActivity()){
            isEnabled = false
            activity.onBackPressed()
            return
        }

        ExitDialog.newInstance()
            .show((activity as AppCompatActivity).supportFragmentManager, "exit dialog")
    }
}