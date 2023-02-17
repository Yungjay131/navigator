package dev.joshuasylvanus.navigator.impl

import android.app.Activity
import android.app.Application
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import dev.joshuasylvanus.navigator.ContainerAlreadySetException
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import java.lang.NullPointerException


/**
 * Created by Joshua Sylvanus, 6:04 AM, 25/08/2022.
 */

class FragmentContinuationStatefulImpl(private val fragmentManager: FragmentManager)
    : FragmentContinuationStateful {
    //region Vars
    private val DEFAULT = "NOT_SET"

    private var transaction: FragmentTransaction? = null
    private var containerID: Int = 0

    private var afterFunc:(() -> Unit)? = null

    private var currentFragmentTag:String = DEFAULT
    private var fragmentTagList:MutableList<String> = mutableListOf()
    //endregion

    init { init() }

    private fun init(){
        transaction = fragmentManager.beginTransaction()

        transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    }

    override fun into(@IdRes containerID:Int): FragmentContinuationStateful {
        if(this.containerID == 0)
            this.containerID = containerID

        return this
    }

    override fun replace(f: Fragment): FragmentContinuationStateful {
        transaction!!.addToBackStack("${f::class.simpleName}")
        transaction!!.replace(containerID, f)
        currentFragmentTag = f::class.simpleName!!
        return this
    }

    override fun hideCurrent(): FragmentContinuationStateful {
        if(currentFragmentTag != DEFAULT) {
            val f: Fragment = fragmentManager.findFragmentByTag(currentFragmentTag)!!
            transaction!!.hide(f)
            fragmentTagList.removeLast()
            fragmentTagList.add(currentFragmentTag)
        }

        return this
    }

    override fun show(f: Fragment, currentTag: String?): FragmentContinuationStateful {
        /*no op, no need since the whole purpose of this implementation is to remove the need for this method*/
        show(f)
        return this
    }

    override fun show(f: Fragment): FragmentContinuationStateful {
        if(fragmentManager.findFragmentByTag(f::class.simpleName) != null &&
           currentFragmentTag != DEFAULT){
            /*its been added before*/
            val _f:Fragment = fragmentManager.findFragmentByTag(currentFragmentTag)!!
            transaction!!.hide(_f)

            val __f:Fragment = fragmentManager.findFragmentByTag(f::class.simpleName)!!
            transaction!!.show(__f)
        }else{
            /*hide currently visible Fragment*/
            if(fragmentManager.findFragmentById(containerID) != null)
                transaction!!.hide(fragmentManager.findFragmentByTag(currentFragmentTag)!!)

            transaction!!.addToBackStack("${f::class.simpleName}")
            transaction!!.add(containerID, f, "${f::class.simpleName}")
        }

        currentFragmentTag = f::class.simpleName!!
        fragmentTagList = fragmentTagList.filter { it != f::class.simpleName } as MutableList<String>
        fragmentTagList.add(currentFragmentTag)

        return this
    }

    override fun after(block:() -> Unit): FragmentContinuationStateful {
        afterFunc = block
        return this
    }

    override fun popBackStack(also:((String)->Unit)?):Boolean{
        var wasPopped = false
        if(fragmentManager.backStackEntryCount > 1){
            fragmentManager.popBackStack()
            fragmentTagList.removeLast()
            currentFragmentTag = fragmentTagList.last()

            also?.invoke(currentFragmentTag)

            wasPopped = true
        }

        return wasPopped
    }

    /**
     * @param tag KClass#simpleName String of the fragment class you want to pop to
     * @return true if backStack was popped or false if current Fragment is the only Fragment in the backstack */
    override fun popBackStack(tag:String):Boolean{
        var wasPopped = false
        if(fragmentManager.backStackEntryCount > 1){
            fragmentManager.popBackStack(tag,0)

            val fragmentIndex:Int = fragmentTagList.indexOf(tag)
            var currentIndex:Int = fragmentTagList.size - 1
            while(currentIndex != fragmentIndex){
               fragmentTagList.removeLast()
               currentIndex--
            }

            currentFragmentTag = fragmentTagList.last()
            wasPopped = true
        }

        return wasPopped
    }

    override fun onDestroy(block: (() -> Unit)?) {
        block?.invoke()
    }

    override fun navigate(){
        transaction!!.commit()
        afterFunc?.invoke()

        init()
    }
}

