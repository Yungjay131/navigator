package dev.joshuasylvanus.navigator.interfaces

import android.view.View


/**
 * Created by Joshua Sylvanus, 5:59 AM, 25/08/2022.
 */
interface ActivityContinuation {
    fun addSharedElementTransition(view:View, transitionViewName:String): ActivityContinuation
    fun addExtra(key:String, value:Any): ActivityContinuation
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