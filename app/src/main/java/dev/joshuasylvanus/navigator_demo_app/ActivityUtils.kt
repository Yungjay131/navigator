package dev.joshuasylvanus.navigator_demo_app


/**
 * Created by Joshua Sylvanus, 10:08 PM, 18/05/2022.
 */
object ActivityUtils {
    private var count:Int = 0

    fun isLastActivity():Boolean = count == 1
    fun incrementActivityCount():Int = count++
    fun decrementActivityCount():Int = count--
}

