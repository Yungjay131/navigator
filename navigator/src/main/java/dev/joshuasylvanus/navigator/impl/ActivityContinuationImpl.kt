package dev.joshuasylvanus.navigator.impl

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.annotation.AnimRes
import androidx.core.app.ActivityOptionsCompat
import com.freexitnow.navigation_lib.R
import com.freexitnow.navigation_lib.interfaces.ActivityContinuation
import java.util.ArrayList


/**
 * Created by Joshua Sylvanus, 6:02 AM, 25/08/2022.
 */

class ActivityContinuationImpl(@PublishedApi
                               internal val intent: Intent,
                               private var activity: Activity?) : ActivityContinuation {
    private var shouldFinishCaller:Boolean = false
    private var transitionPairs:MutableList<Pair<View, String>> = mutableListOf()

    @AnimRes
    private var enterTransition:Int = R.anim.activity_enter
    @AnimRes
    private var exitTransition:Int = R.anim.activity_exit

    override fun setActivityTransition(@AnimRes enterAnim:Int, @AnimRes exitAnim:Int): ActivityContinuation{
       this.enterTransition = enterAnim
       this.exitTransition = exitAnim
       return this
    }

    override fun addSharedElementTransition(view: View, transitionViewName: String): ActivityContinuation {
        this.transitionPairs.add(Pair<View,String>(view,transitionViewName))
        return this
    }

    override fun addIntentData(uri: Uri): ActivityContinuation {
        this.intent.setData(uri)
        return this
    }

    override fun addExtra(key: String, value: Any): ActivityContinuation {
        when(value){
            is Int -> this.intent.putExtra(key, value)
            is Double -> this.intent.putExtra(key, value)
            is Long -> this.intent.putExtra(key, value)
            is Char -> this.intent.putExtra(key, value)
            is Byte -> this.intent.putExtra(key, value)
            is Boolean -> this.intent.putExtra(key, value)
            is String -> this.intent.putExtra(key, value)
            is Bundle -> this.intent.putExtra(key, value)
            is Parcelable -> this.intent.putExtra(key, value)
            is List<*> -> {
               val _v1: Parcelable? = value[0] as? Parcelable
               val _v2: Int? = value[0] as? Int
               val _v3: CharSequence? = value[0] as? CharSequence
               when{
                   _v1 != null ->
                       this.intent.putParcelableArrayListExtra(key,value as ArrayList<out Parcelable>)
                   _v2 != null ->
                       this.intent.putIntegerArrayListExtra(key,value as ArrayList<Int>)
                   _v3 != null ->
                        this.intent.putCharSequenceArrayListExtra(key, value as ArrayList<CharSequence>)
                   else -> throw IllegalArgumentException("List type is not supported")
               }
            }

            else -> throw IllegalArgumentException("the type for `value` is not supported")
        }

        return this
    }

    override fun setPackageName(packageName:String): ActivityContinuation {
        this.intent.setPackage(packageName)
        return this
    }

    override fun clearTop(): ActivityContinuation {
        this.intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        return this
    }

    override fun newAndClearTask(): ActivityContinuation {
        this.intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        return this
    }

    override fun previousIsTop(): ActivityContinuation {
        this.intent.addFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
        return this
    }

    override fun singleTop(): ActivityContinuation {
        this.intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        return this
    }

    override fun noHistory():ActivityContinuation{
        this.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
        return this
    }

    override fun reorderToFront():ActivityContinuation{
        this.intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        return this
    }

    override fun finishCaller(): ActivityContinuation {
        shouldFinishCaller = true
        return this
    }

    override fun navigate(){
        var options:ActivityOptionsCompat? = null
        if(transitionPairs.isNotEmpty())
           options = ActivityOptionsCompat.makeSceneTransitionAnimation(this.activity!!, *transitionPairs.toTypedArray())

        activity!!.startActivity(this.intent, options?.toBundle())
        activity!!.overridePendingTransition(enterTransition, exitTransition)

        if(shouldFinishCaller)
            activity!!.finish()

        activity = null
    }
}