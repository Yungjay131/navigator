package dev.joshuasylvanus.navigator.interfaces

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager


/**
 * Created by Joshua Sylvanus, 6:00 AM, 25/08/2022.
 */
interface FragmentContinuationStateful  {
    fun into(@IdRes containerID: Int): FragmentContinuationStateful

    fun setCustomAnimations(@AnimatorRes @AnimRes enter:Int?,
                            @AnimatorRes @AnimRes exit: Int?,
                            @AnimatorRes @AnimRes popEnter: Int?,
                            @AnimatorRes @AnimRes popExit: Int?) : FragmentContinuationStateful

    fun hideCurrent(): FragmentContinuationStateful

    fun show(f: Fragment): FragmentContinuationStateful

    fun replace(f: Fragment): FragmentContinuationStateful

    fun after(block: () -> Unit): FragmentContinuationStateful

    fun navigate()

    fun popBackStack(also: ((String) -> Unit)? = null):Boolean

    fun popBackStack(tag:String):Boolean

    fun onDestroy(block: (() -> Unit)? = null)
}