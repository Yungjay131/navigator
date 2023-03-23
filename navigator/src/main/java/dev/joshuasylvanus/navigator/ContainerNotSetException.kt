package dev.joshuasylvanus.navigator

/**
 * Created by Joshua Sylvanus, 10:57 AM, 26/08/2022.
 */
internal class ContainerNotSetException(): Exception(){
    override val message: String
        get() = "a FragmentContainer has not been set for this transaction"
}