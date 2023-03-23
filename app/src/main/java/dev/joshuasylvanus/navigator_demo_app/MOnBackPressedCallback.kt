package dev.joshuasylvanus.navigator_demo_app

import android.app.Activity
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful

open class MOnBackPressedCallback(private val activity: AppCompatActivity,
                                  private val navigator:FragmentContinuationStateful? = null,
                                  private val popFunc:((String) -> Unit)? = null)
    : OnBackPressedCallback(true) {

    private val func:() -> Unit = if(navigator != null) ::newImpl else ::oldImpl
    override fun handleOnBackPressed() { func() }

    private fun newImpl(){
        if(!navigator!!.popBackStack{ popFunc?.invoke(it) }){
            if(!ActivityUtils.isLastActivity())
                activity.finish()
            else
                ExitDialog().show(activity.supportFragmentManager, "")
        }
    }

    private fun oldImpl(){
        if(!ActivityUtils.isLastActivity()){
            isEnabled = false
            activity.onBackPressed()
            return
        }

        ExitDialog.newInstance().show(activity.supportFragmentManager, "exit dialog")

    }
}