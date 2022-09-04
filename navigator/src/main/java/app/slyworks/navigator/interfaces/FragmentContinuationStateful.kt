package app.slyworks.navigator.interfaces


/**
 *Created by Joshua Sylvanus, 6:00 AM, 25/08/2022.
 */
interface FragmentContinuationStateful : FragmentContinuation {
    fun popBackStack(also: ((String) -> Unit)? = null)
    fun onDestroy(block: (() -> Unit)? = null)
}