package dev.joshuasylvanus.navigator.interfaces

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment


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

    fun setFragmentObserver(observer: FragmentLifecycleObserver): FragmentContinuationStateful

    fun navigate()

    fun popBackStackInBackground(tag:String): FragmentContinuationStateful

    fun popBackStack(also: ((String) -> Unit)? = null):Boolean

    fun popBackStack(tag:String):Boolean

    fun onDestroy(block: (() -> Unit)? = null)
}