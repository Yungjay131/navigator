package dev.joshuasylvanus.navigator.interfaces

import android.net.Uri
import android.view.View
import androidx.annotation.AnimRes


/**
 * Created by Joshua Sylvanus, 5:59 AM, 25/08/2022.
 */
interface ActivityContinuation {
    fun setActivityTransition(@AnimRes enterAnim:Int, @AnimRes exitAnim:Int): ActivityContinuation

    fun addSharedElementTransition(view:View, transitionViewName:String): ActivityContinuation

    fun addExtra(key:String, value:Any): ActivityContinuation

    fun addIntentData(uri: Uri): ActivityContinuation

    fun setPackageName(packageName:String): ActivityContinuation

    fun clearTop(): ActivityContinuation

    fun newAndClearTask(): ActivityContinuation

    fun previousIsTop(): ActivityContinuation

    fun singleTop(): ActivityContinuation

    fun noHistory(): ActivityContinuation

    fun reorderToFront(): ActivityContinuation

    fun finishCaller(): ActivityContinuation

    fun navigate()
}