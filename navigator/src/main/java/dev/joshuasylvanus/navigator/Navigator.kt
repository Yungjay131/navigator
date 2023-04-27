package dev.joshuasylvanus.navigator

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dev.joshuasylvanus.navigator.interfaces.ActivityContinuation
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import dev.joshuasylvanus.navigator.impl.ActivityContinuationImpl
import dev.joshuasylvanus.navigator.impl.FragmentContinuationStatefulImpl


/**
 * Created by Joshua Sylvanus, 9:27 PM, 20/08/2022.
 */

const val DEFAULT_INT_VALUE = -120934
const val DEFAULT_FLOAT_VALUE = -0.1020304050F
const val DEFAULT_DOUBLE_VALUE = -120934.012
const val DEFAULT_LONG_VALUE = 1020304050L

class Navigator private constructor() {

    init {
        throw UnsupportedOperationException("this class is not to be instantiated")
    }

    companion object{
        private var packageIdentifier:String? = null

        @JvmStatic
        fun getPackageIdentifier():String? = packageIdentifier

        @JvmStatic
        fun setPackageIdentifier(identifier:String?){
            packageIdentifier = identifier
        }

        @JvmStatic
        fun minimizeApp(from:Context){
           (from as AppCompatActivity).moveTaskToBack(true)
        }

        @JvmStatic
        fun restartApp(context:Context,
                       extraKey:String? = null,
                       extraValue:String? = null){
            val packageManager:PackageManager = context.packageManager
            val originalIntent:Intent = packageManager.getLaunchIntentForPackage(context.packageName)!!
            val componentName:ComponentName = originalIntent.component!!

            val intent:Intent = Intent.makeRestartActivityTask(componentName)
            if(extraKey != null && extraValue != null)
                intent.putExtra(extraKey, extraValue)
            context.startActivity(intent)

            Runtime.getRuntime().exit(0)
        }

        @JvmStatic
        inline fun <reified T: Activity> intentFor(from: Context): ActivityContinuation =
            ActivityContinuationImpl(Intent(from, T::class.java), from as AppCompatActivity)

        @JvmStatic
        fun intentFor(from: Context, clazz: Class<out AppCompatActivity>): ActivityContinuation =
            ActivityContinuationImpl(Intent(from, clazz), from as AppCompatActivity)

        @JvmStatic
        fun intentFor(from:Context, intentFilter:String): ActivityContinuation =
            ActivityContinuationImpl(Intent(intentFilter).setPackage(from.packageName), from as AppCompatActivity)

        @JvmStatic
        fun intentFromIntentFilter(from:Context, intentFilter:String): Intent =
            Intent(intentFilter).setPackage(from.packageName)

        @JvmStatic
        fun transactionWithStateFrom(fragmentManager: FragmentManager): FragmentContinuationStateful =
           FragmentContinuationStatefulImpl(fragmentManager = fragmentManager)

        /**
         * allows you to run `replace` fragment operations without
         * being saved to the backstack
         *
         * @param fm FragmentManager to use for this transaction
         * @param containerID the View id for the FragmentContainer or FrameLayout, the fragment is going into
         * @param f Fragment being replaced
         * @param anims: customise the transition animations for the transaction
         *               it should be an array with enter, exit, popEnter, popExit */
        @JvmStatic
        fun replaceAnonymous(fm:FragmentManager,
                             containerID: Int,
                             f: Fragment,
                             @AnimatorRes @AnimRes anims:IntArray? = null){
            val args:IntArray = intArrayOf(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit )

            val transaction: FragmentTransaction = fm.beginTransaction()

            anims?.let{ array ->
                if(array[0] != -1)
                    args[0] = array[0]
                if(array[1] != -1)
                    args[1] = array[1]
                if(array[2] != -1)
                    args[2] = array[2]
                if(array[3] != -1)
                    args[3] = array[3]
            }
            transaction.setCustomAnimations(
                args[0],
                args[1],
                args[2],
                args[3])
            transaction.replace(containerID, f)
        }

        /**
         * allows you to run `show` fragment operations without
         * being saved to the backstack
         *
         * @param fm FragmentManager to use for this transaction
         * @param containerID the View id for the FragmentContainer or FrameLayout, the fragment is going into
         * @param currentFragmentTag the KClass#simpleName of the currently shown fragment
         * @param f Fragment being shown
         * @param anims: customise the transition animations for the transaction
         *               it should be an array with enter, exit, popEnter, popExit */
         @JvmStatic
         fun showAnonymous(fm:FragmentManager,
                           containerID: Int,
                           f: Fragment,
                           currentFragmentTag:String,
                           anims:IntArray? = null){
            val args:IntArray = intArrayOf(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit )

            val transaction: FragmentTransaction = fm.beginTransaction()

            anims?.let{ array ->
                if(array[0] != -1)
                    args[0] = array[0]
                if(array[1] != -1)
                    args[1] = array[1]
                if(array[2] != -1)
                    args[2] = array[2]
                if(array[3] != -1)
                    args[3] = array[3]
            }
            transaction.setCustomAnimations(
                args[0],
                args[1],
                args[2],
                args[3])

            if(fm.findFragmentById(containerID) != null) {
                transaction.hide(fm.findFragmentByTag(currentFragmentTag)!!)
                transaction.show(f)
            }else
                transaction.add(containerID, f)
        }


        @JvmStatic
        @AnimRes
        fun getActivityEnterTransition(): Int =
            R.anim.activity_enter

        @JvmStatic
        @AnimRes
        fun getActivityExitTransition(): Int =
            R.anim.activity_exit

        @JvmStatic
        @AnimatorRes @AnimRes
        fun getFragmentEnterTransition():Int =
            R.anim.enter

        @JvmStatic
        @AnimatorRes @AnimRes
        fun getFragmentExitTransition():Int =
            R.anim.exit

        @JvmStatic
        @AnimatorRes @AnimRes
        fun getFragmentPopEnterTransition():Int =
            R.anim.pop_enter

        @JvmStatic
        @AnimatorRes @AnimRes
        fun getFragmentPopExitTransition():Int =
            R.anim.pop_exit

        @JvmStatic
        inline fun <reified T> Intent.getExtra(key:String, defaultValue:T? = null):T?{
            return when(T::class){
                Boolean::class ->
                    this.getBooleanExtra(key, defaultValue!! as Boolean ) as T

                String::class ->{
                    val s: String = this.getStringExtra(key) ?: return defaultValue

                    s as? T
                }

                Int::class -> {
                    val i:Int = this.getIntExtra(key, DEFAULT_INT_VALUE)
                    if(i == DEFAULT_INT_VALUE)
                        return defaultValue

                    i as? T
                }

                Long::class -> {
                    val l:Long = this.getLongExtra(key, DEFAULT_LONG_VALUE)
                    if(l == DEFAULT_LONG_VALUE)
                        return defaultValue

                    l as? T
                }

                Double::class-> {
                    val d:Double = this.getDoubleExtra(key, DEFAULT_DOUBLE_VALUE)
                    if(d == DEFAULT_DOUBLE_VALUE)
                        return defaultValue

                    d as? T
                }

                Float::class-> {
                    val f:Float = this.getFloatExtra(key, DEFAULT_FLOAT_VALUE)
                    if(f == DEFAULT_FLOAT_VALUE)
                        return defaultValue

                    f as? T
                }

                Byte::class -> {
                    val b:Byte = this.getByteExtra(key, Byte.MIN_VALUE)
                    if(b == Byte.MIN_VALUE)
                        return defaultValue

                    b as? T
                }

                Bundle::class->{
                    val bu:Bundle = this.getBundleExtra(key) ?: return defaultValue
                    bu as? T
                }

                ByteArray::class -> {
                    val ba:ByteArray = this.getByteArrayExtra(key) ?: return defaultValue
                    ba as? T
                }

                else -> throw IllegalArgumentException("type of ${T::class} is not supported")
            }
        }

        @JvmStatic
        fun <T : Parcelable> Intent.getParcelable(key:String):T? {
            return this.getParcelableExtra<T>(key) as? T
        }
    }
}