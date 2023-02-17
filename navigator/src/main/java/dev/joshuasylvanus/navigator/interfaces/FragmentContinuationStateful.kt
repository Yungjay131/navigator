package dev.joshuasylvanus.navigator.interfaces

import dev.joshuasylvanus.navigator.interfaces.FragmentContinuation


/**
 * Created by Joshua Sylvanus, 6:00 AM, 25/08/2022.
 */
interface FragmentContinuationStateful : FragmentContinuation {
    fun popBackStack(also: ((String) -> Unit)? = null):Boolean
    fun popBackStack(tag:String):Boolean
    fun onDestroy(block: (() -> Unit)? = null)
}