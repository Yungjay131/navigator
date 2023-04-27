package dev.joshuasylvanus.navigator.exceptions

/**
 * Created by Joshua Sylvanus, 7:55 AM, 27/04/2024.
 */
internal class PackageNameNotSetException(): Exception(){
    override val message: String
        get() = "please set a package identifier, that all fragment KClass#simpleName will contain" +
                " to ensure fragments are reinitialized correctly if the app is killed in the background" +
                " you can do this by calling `Navigator.setPackageIdentifier(packageIdentifier:String)" +
                " from your application class. this method needs to be called only once"
}