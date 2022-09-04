package app.slyworks.navigator.impl

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.slyworks.navigator.ContainerAlreadySetException
import app.slyworks.navigator.interfaces.FragmentContinuation
import app.slyworks.navigator.interfaces.FragmentContinuationStateful


/**
 *Created by Joshua Sylvanus, 6:04 AM, 25/08/2022.
 */

data class FragmentContinuationStatefulImpl(private var _fragmentManager: FragmentManager?)
    : FragmentContinuationStateful {
    //region Vars
    private var fragmentManager: FragmentManager = _fragmentManager!!

    private var _transaction: FragmentTransaction? = null
    private var transaction: FragmentTransaction
    private var containerID: Int = 0

    private var mCurrentFragmentTag:String? = null
    private var mFragmentTagList:MutableList<String> = mutableListOf()
    //endregion

    init {
        _transaction = fragmentManager.beginTransaction()

        transaction = _transaction!!
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    }

    override fun into(@IdRes containerID:Int): FragmentContinuation {
        if(this.containerID != 0)
            throw ContainerAlreadySetException()

        this.containerID = containerID
        return this
    }

    fun replace(f: Fragment): FragmentContinuation {
        transaction.addToBackStack("${f::class.simpleName}")
        transaction.replace(containerID, f)
        mCurrentFragmentTag = f::class.simpleName
        return this
    }

    override fun hideCurrent(): FragmentContinuation {
        if(mCurrentFragmentTag != null) {
            val f: Fragment = fragmentManager.findFragmentByTag(mCurrentFragmentTag!!)!!
            transaction.hide(f)
            mCurrentFragmentTag = f::class.simpleName
            mFragmentTagList.removeLast()
            mFragmentTagList.add(mCurrentFragmentTag!!)
        }

        return this
    }

    override fun show(f: Fragment, currentTag: String?): FragmentContinuation {
        /*no op, no need since the whole purpose of this implementation is to remove the need for this method*/
        return this
    }

    override fun show(f: Fragment): FragmentContinuation {
        if(fragmentManager.findFragmentByTag(f::class.simpleName) != null){
            /*its been added before*/
            transaction.hide(fragmentManager.findFragmentByTag(mCurrentFragmentTag!!)!!)
            transaction.show(fragmentManager.findFragmentByTag(f::class.simpleName)!!)
        }else{
            if(fragmentManager.findFragmentById(containerID) != null)
            /*hide currently visible Fragment*/
                transaction.hide(fragmentManager.findFragmentByTag(mCurrentFragmentTag!!)!!)

            transaction.addToBackStack("${f::class.simpleName}")
            transaction.add(containerID, f, "${f::class.simpleName}")
        }

        mCurrentFragmentTag = f::class.simpleName
        mFragmentTagList = mFragmentTagList.filter { it != f::class.simpleName } as MutableList<String>
        mFragmentTagList.add(mCurrentFragmentTag!!)

        return this
    }

    override fun after(block:() -> Unit): FragmentContinuation {
        block()
        return this
    }

    override fun popBackStack(also:((String)->Unit)?){
        if(fragmentManager.backStackEntryCount >= 1){
            fragmentManager.popBackStack()
            mFragmentTagList.removeLast()
            mCurrentFragmentTag = mFragmentTagList.last()

            also?.invoke(mCurrentFragmentTag!!)
        }
    }

    override fun onDestroy(block: (() -> Unit)?) {
        block?.invoke()
        _fragmentManager = null
    }

    override fun navigate(){
        transaction.commit()

        _transaction = null
        _fragmentManager = null
    }
}

