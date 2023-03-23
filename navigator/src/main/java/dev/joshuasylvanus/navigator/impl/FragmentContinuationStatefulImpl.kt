package dev.joshuasylvanus.navigator.impl

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dev.joshuasylvanus.navigator.ContainerNotSetException
import dev.joshuasylvanus.navigator.R
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful


/**
 * Created by Joshua Sylvanus, 6:04 AM, 25/08/2022.
 */
class FragmentContinuationStatefulImpl(private val fragmentManager: FragmentManager)
    : FragmentContinuationStateful {
    //region Vars
    private val DEFAULT_FRAGMENT_TAG = "NOT_SET"
    private val DEFAULT_CONTAINER_ID = 0
    @IdRes
    private var intoArgs: Int = 0
    private var isThereInto: Boolean = false

    private lateinit var replaceArgs: Fragment
    private var isThereReplace: Boolean = false

    private var isThereHideCurrent: Boolean = false

    private var isThereShow1: Boolean = false
    private val show1Args: Array<Any?> = emptyArray()

    private lateinit var show2Args: Fragment
    private var isThereShow2: Boolean = false


    private var transaction: FragmentTransaction? = null
    private var containerID: Int = DEFAULT_CONTAINER_ID

    private var afterArgs:(() -> Unit)? = null

    private var currentFragmentTag:String = DEFAULT_FRAGMENT_TAG
    private var fragmentTagList:MutableList<String> = mutableListOf()
    //endregion

    init { initialize() }

    private fun initialize(){
        /* resetting values */
        isThereInto = false
        isThereReplace = false
        isThereHideCurrent = false
        isThereShow1 = false
        isThereShow2 = false

        transaction = fragmentManager.beginTransaction()
        transaction!!.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction!!.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit )
    }

    private fun reinitialize(){
        val fragmentList:MutableList<Fragment> = fragmentManager.getFragments()
        for(f in fragmentList){
            if(f != null)
                fragmentTagList.add(f.tag!!)
        }

        if(fragmentTagList.isNotEmpty())
           currentFragmentTag = fragmentTagList.last()
    }

   /**
    * set a container for the fragments to be hosted during the lifetime of this FragmentContinuationStateful
    * instance
    *
    * @param containerID the View Id for the FragmentContainerView or FrameLayout
    * @return the same FragmentContinuationStateful object*/
    override fun into(@IdRes containerID:Int): FragmentContinuationStateful {
        intoArgs = containerID
        isThereInto = true
        return this
    }

    private fun _into(@IdRes containerID: Int){
        if(this.containerID == 0)
            this.containerID = containerID
    }

    /**
     * replace the currently visible fragment with a new Fragment
     *
     * @param f the fragment being used to replace the existing fragment
     * @return the same FragmentContinuationStateful object*/
    override fun replace(f: Fragment): FragmentContinuationStateful {
        replaceArgs = f
        isThereReplace = true
        return this
    }

    private fun _replace(f: Fragment) {
        transaction!!.addToBackStack("${f::class.simpleName}")
        transaction!!.replace(containerID, f)

        fragmentTagList = fragmentTagList.filter { it != currentFragmentTag } as MutableList<String>
        currentFragmentTag = f::class.simpleName!!
        fragmentTagList.add(currentFragmentTag)
    }

    /**
     * hide the currently visible fragment. in almost all cases you don't need
     * this method
     *
     * @return the same FragmentContinuationStateful object
     * @deprecated use only FragmentContinuationStateful#show */
    override fun hideCurrent(): FragmentContinuationStateful {
        isThereHideCurrent = true
        return this
    }

    private fun _hideCurrent() {
        if(currentFragmentTag != DEFAULT_FRAGMENT_TAG) {
            val f: Fragment = fragmentManager.findFragmentByTag(currentFragmentTag)!!
            transaction!!.hide(f)
        }
    }

    /**
     * show fragment with tag, currentTag should be the KClass#simplename of the fragment
     * currently visible
     *
     * @param f fragment to be shown
     * @param currentTag not needed, but was intended to be the KClass#simplename of the fragment
     *                   currently visible
     * @return the same FragmentContinuationStateful object
     * @deprecated use only FragmentContinuationStateful#show */
    override fun show(f: Fragment, currentTag: String?): FragmentContinuationStateful {
        /*no op, no need since the whole purpose of this implementation is to remove the need for this method*/
        isThereShow1 = true
        show1Args[0] = f
        show1Args[1] = currentTag
        return this
    }

    /**
     * show fragment
     *
     * @param f fragment to be shown
     * @return the same FragmentContinuationStateful object */
    override fun show(f: Fragment): FragmentContinuationStateful {
        isThereShow2 = true
        show2Args = f
        return this
    }

    private fun _show(f: Fragment) {
        /* checking if this is a process recreation, during process recreation, android handles adding fragments back to
        * the fragmentManager but this renders the currentFragmentTag and fragmentTagList useless */
        if(fragmentManager.findFragmentByTag(f::class.simpleName) != null && currentFragmentTag == DEFAULT_FRAGMENT_TAG)
            reinitialize()

        if(fragmentManager.findFragmentByTag(f::class.simpleName) != null){
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
    }

    /**
     * function to be run after this series of operations.
     * would be run after the FragmentContinuationStateful#navigate call
     *
     * @param block function to be run after this series of operation or fragmentTransaction(s)
     *              i.e everything before the FragmentContinuationStateful#navigate call
     * @return the same FragmentContinuationStateful object */
    override fun after(block:() -> Unit): FragmentContinuationStateful {
        afterArgs = block
        return this
    }

    /**
     * pop the FragmentManager backstack of the current FragmentContinuationStateful instance
     * returns a boolean status based on whether a fragment was popped or not.
     * pop does not happen if the currently visible fragment is the last fragment in the
     * fragmentManager
     *
     * @param also function to be run immediately after the pop
     * @return true if the currently visible fragment is not the last
     *         false if the currently visible fragment is the last fragment*/
    override fun popBackStack(also:((String)->Unit)?):Boolean{
        var wasPopped = false
        if(fragmentTagList.size > 1){
            fragmentManager.popBackStack()
            fragmentTagList.removeLast()
            currentFragmentTag = fragmentTagList.last()

            also?.invoke(currentFragmentTag)

            wasPopped = true
        }

        return wasPopped
    }

    /**
     * @param tag KClass#simpleName String of the fragment class you want to pop to,
     *            since FragmentTransaction tags are the classnames for the Fragment involved
     * @return true if backStack was popped or false if current Fragment is the only Fragment in the backstack.
     * also returns false if the tag does not exist in the fragmentManager backStack */
    override fun popBackStack(tag:String):Boolean {
        var wasPopped = false
        val fragmentIndex:Int = fragmentTagList.indexOf(tag)

        if(fragmentTagList.size > 1 && fragmentIndex != -1){
            fragmentManager.popBackStack(tag,0)

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

    /**
     * this method was part of the initial API surface for this Library as a way to prevent
     * memory leaks, resulting from the FragmentContinuationStateful instance outliving the fragment or activity
     * but so far seems like a far fetched use case
     *
     * @param block function to be executed when the FragmentContinuationStateful instance is being destroyed
     * @return true if backStack was popped or false if current Fragment is the only Fragment in the backstack */
    override fun onDestroy(block: (() -> Unit)?) {
        block?.invoke()
    }

    /**
     * terminal function for operations on a FragmentContinuationStateful.
     * in the new implementation the functions tied to operations set on the
     * FragmentContinuationStateful instance are only executed when this method is called
     * this is done to ensure a constant order of execution for function calls
     *
     *
     * NOTE
     *  certain methods here are not meant to be called together
     *  and the result of calling them is undetermined e.g combining replace()
     *  and hideCurrent(), this code is to ensure a constant order of execution
     * irrespective of how the caller arranged the chain of method calls */
    override fun navigate(){
        if(!isThereInto && currentFragmentTag == DEFAULT_FRAGMENT_TAG)
            throw ContainerNotSetException()

        if(isThereInto)
            _into(intoArgs)
        if(isThereReplace)
            _replace(replaceArgs)
        if(isThereHideCurrent)
            _hideCurrent()
        if(isThereShow1)
            _show(show1Args[0] as Fragment)
        if(isThereShow2)
            _show(show2Args)

        transaction!!.commit()
        afterArgs?.invoke()

        initialize()
    }
}
