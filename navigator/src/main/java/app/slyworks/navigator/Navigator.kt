package app.slyworks.navigator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import app.slyworks.navigator.interfaces.ActivityContinuation
import app.slyworks.navigator.interfaces.FragmentContinuation
import app.slyworks.navigator.interfaces.FragmentContinuationStateful
import app.slyworks.navigator.impl.ActivityContinuationImpl
import app.slyworks.navigator.impl.FragmentContinuationImpl
import app.slyworks.navigator.impl.FragmentContinuationStatefulImpl


/**
 *Created by Joshua Sylvanus, 9:27 PM, 20/08/2022.
 */
class Navigator private constructor() {

    init {
        throw UnsupportedOperationException("this class is not to be instantiated")
    }

    companion object{
        @JvmStatic
        fun restartApp(from:Context){
            val a = (from as AppCompatActivity)
            a.startActivity(Intent(from.applicationContext, a::class.java))
            a.finish()
        }
        @JvmStatic
        inline fun <reified T: Activity> intentFor(from: Context): ActivityContinuation {
            return ActivityContinuationImpl(Intent(from, T::class.java), from as AppCompatActivity)
        }

        @JvmStatic
        fun intentFor(from: Context, clazz: Class<out AppCompatActivity>): ActivityContinuation {
            return ActivityContinuationImpl(Intent(from, clazz), from as AppCompatActivity)
        }

        @JvmStatic
        fun transactionFrom(fragmentManager: FragmentManager): FragmentContinuation {
            return FragmentContinuationImpl(fragmentManager)
        }

        @JvmStatic
        fun transactionWithStateFrom(fragmentManager: FragmentManager): FragmentContinuationStateful {
            return FragmentContinuationStatefulImpl(fragmentManager)
        }

        @JvmStatic
        inline fun <reified T> Intent.getExtra(key:String, defaultValue:T? = null):T?{
            return when(T::class){
                String::class ->{
                    val s: String = this.getStringExtra(key) ?: return defaultValue

                    s as? T
                }
                Int::class -> {
                    val i:Int = this.getIntExtra(key, -120934)
                    if(i == -120934)
                        return defaultValue

                    i as? T
                }
                Double::class-> {
                    val d:Double = this.getDoubleExtra(key, -120934.012)
                    if(d == -120934.012)
                        return defaultValue

                    d as? T
                }
                Bundle::class->{
                    val bu:Bundle = this.getBundleExtra(key) ?: return defaultValue
                    bu as? T
                }
                ByteArray::class -> {
                    val ba:Byte = this.getByteExtra(key, Byte.MIN_VALUE)
                    if(ba == Byte.MIN_VALUE)
                        return defaultValue

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