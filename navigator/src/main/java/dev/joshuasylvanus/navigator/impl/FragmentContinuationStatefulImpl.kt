package dev.joshuasylvanus.navigator.impl

import androidx.annotation.AnimRes
import androidx.annotation.AnimatorRes
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import dev.joshuasylvanus.navigator.exceptions.ContainerNotSetException
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.R
import dev.joshuasylvanus.navigator.exceptions.PackageNameNotSetException
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import dev.joshuasylvanus.navigator.interfaces.FragmentLifecycleObserver


/**
 * Created by Joshua Sylvanus, 6:04 AM, 25/08/2022.
 */
class FragmentContinuationStatefulImpl(private val fragmentManager: FragmentManager)
    : FragmentContinuationStateful {
    //region Vars
    private val DEFAULT_STRING_TAG = "NOT_SET"
    private val DEFAULT_INT_TAG = 0

    private val currentFragmentChangedObserver:((String) -> Unit)? =
        Navigator.getOnCurrentFragmentChangedFunc()

    @AnimatorRes @AnimRes
    private val customAnimationArgs:IntArray = intArrayOf(
        R.anim.enter,
        R.anim.exit,
        R.anim.pop_enter,
        R.anim.pop_exit )
    private var isThereSetCustomAnimations:Boolean = false

    @IdRes
    private var intoArgs: Int = 0
    private var isThereInto: Boolean = false

    private var popBackStackInBackgroundArgs:String = DEFAULT_STRING_TAG
    private var isTherePopBackStackInBackground:Boolean = false

    private var replaceArgs: Fragment? = null
    private var isThereReplace: Boolean = false

    private var isThereHideCurrent: Boolean = false

    private var showArgs: Fragment? = null
    private var isThereShow: Boolean = false

    private var setFragmentObserverArgs: FragmentLifecycleObserver? = null
    private var isThereSetFragmentObserver:Boolean = false

    //private var transaction: FragmentTransaction? = null
    private var containerID: Int = DEFAULT_INT_TAG

    private var afterArgs:(() -> Unit)? = null

    private var currentFragmentTag:String = DEFAULT_STRING_TAG
    private var fragmentTagList:MutableList<String> = mutableListOf()

    private val transactionsList:MutableList<FragmentTransaction> = mutableListOf()
    //endregion

    init {
        initialize()
    }

    private fun initialize(){
        /* resetting values */
        isThereInto = false

        isThereSetCustomAnimations = false

        isTherePopBackStackInBackground = false
        popBackStackInBackgroundArgs = DEFAULT_STRING_TAG

        isThereReplace = false
        replaceArgs = null

        isThereHideCurrent = false

        isThereShow = false
        showArgs = null

        isThereSetFragmentObserver = false
        setFragmentObserverArgs = null

        transactionsList.clear()
    }

    /**
     * Libraries like Glide create their own Fragments, and this becomes a problem
     * during reinitialisation, so to ensure that only Fragments that relate to
     * your app are added back in the FragmentList, set a packageIdentifier */
    private fun reinitialize(){
        val packageIdentifier:String =
            Navigator.getPackageIdentifier() ?: throw PackageNameNotSetException()

        val fragmentList:MutableList<Fragment> = fragmentManager.getFragments()
        for(f in fragmentList){
            if(f != null && f::class.qualifiedName?.contains(packageIdentifier, true) == true)
                fragmentTagList.add(f.tag!!)
        }

        if(fragmentTagList.isNotEmpty()) {
            currentFragmentTag = fragmentTagList.last()
            currentFragmentChangedObserver?.invoke(currentFragmentTag)
        }
    }

    /**
     * sets the custom animations for the various transitions fragments go though
     * you need only provide values for the transitions you are interested in, defaults
     * that are quite sufficient are provided for any parameters not set
     *
     * @param enter the enter animation
     * @param exit the exit animation
     * @param popEnter the pop-enter animation
     * @param popExit the pop-exit animation
     * */
    override fun setCustomAnimations(@AnimatorRes @AnimRes enter:Int?,
                                     @AnimatorRes @AnimRes exit: Int?,
                                     @AnimatorRes @AnimRes popEnter: Int?,
                                     @AnimatorRes @AnimRes popExit: Int?) : FragmentContinuationStateful {
        enter?.let { customAnimationArgs[0] = it }
        exit?.let { customAnimationArgs[1] = it }
        popEnter?.let { customAnimationArgs[2] = it }
        popExit?.let { customAnimationArgs[3] = it }

        isThereSetCustomAnimations = true
        return this
    }

    private fun _setCustomAnimations(){
        transactionsList.forEach{
            it.setCustomAnimations(
                customAnimationArgs[0],
                customAnimationArgs[1],
                customAnimationArgs[2],
                customAnimationArgs[3])
        }

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
        if(this.containerID == DEFAULT_INT_TAG)
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
        val _f:Fragment =
            fragmentManager.findFragmentByTag(currentFragmentTag) ?: return

        fragmentManager.popBackStack(currentFragmentTag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val transaction:FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.replace(containerID,f,f::class.simpleName)
        transaction.addToBackStack(f::class.simpleName!!)

        transactionsList.add(transaction)

        fragmentTagList = fragmentTagList.filter { it != currentFragmentTag } as MutableList<String>
        currentFragmentTag = f::class.simpleName!!
        fragmentTagList.add(currentFragmentTag)

        currentFragmentChangedObserver?.invoke(currentFragmentTag)
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
        if(currentFragmentTag == DEFAULT_STRING_TAG)
            return

        val f: Fragment = fragmentManager.findFragmentByTag(currentFragmentTag)!!
        val transaction:FragmentTransaction = fragmentManager.beginTransaction()
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.addToBackStack(f::class.simpleName!!)
        transaction.hide(f)

        transactionsList.add(transaction)
    }

    /**
     * show fragment
     *
     * @param f fragment to be shown
     * @return the same FragmentContinuationStateful object */
    override fun show(f: Fragment): FragmentContinuationStateful {
        isThereShow = true
        showArgs = f
        return this
    }

    private fun _show(f: Fragment) {
        /* checking if this is a process recreation, during process recreation,
           android handles adding fragments back to the fragmentManager
           but this renders the currentFragmentTag and fragmentTagList useless */
        if(fragmentManager.findFragmentByTag(f::class.simpleName) != null &&
            currentFragmentTag == DEFAULT_STRING_TAG)
            reinitialize()

        if(fragmentManager.findFragmentByTag(f::class.simpleName) != null){
            /*its been added before*/
            val transaction:FragmentTransaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

            val _f:Fragment = fragmentManager.findFragmentByTag(currentFragmentTag)!!
            transaction.hide(_f)

            val __f:Fragment = fragmentManager.findFragmentByTag(f::class.simpleName)!!
            transaction.show(__f)

            transaction.addToBackStack(f::class.simpleName!!)

            transactionsList.add(transaction)
        }else{
            /*hide currently visible Fragment*/
            val transaction:FragmentTransaction = fragmentManager.beginTransaction()
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)

            if(fragmentManager.findFragmentById(containerID) != null) {
                val ___f:Fragment? = fragmentManager.findFragmentByTag(currentFragmentTag)
                if(___f == null)
                  return

                transaction.hide(___f)
            }

            transaction.add(containerID, f, "${f::class.simpleName}")
            transaction.addToBackStack(f::class.simpleName!!)

            transactionsList.add(transaction)
        }

        currentFragmentTag = f::class.simpleName!!
        fragmentTagList = fragmentTagList.filter { it != f::class.simpleName } as MutableList<String>
        fragmentTagList.add(currentFragmentTag)

        currentFragmentChangedObserver?.invoke(currentFragmentTag)
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
     * so as a flaw of Fragments, their onStop(), onPause() e.t.c
     * are tied to their HostFragment, so this method attempts to
     * also notify the fragment popped to, that its now visible
     *
     * @param also function to be run immediately after the pop, which takes the currentFragmentTag
     *         as a parameter
     * @return true if the currently visible fragment is not the last
     *         false if the currently visible fragment is the last fragment*/
    override fun popBackStack(also:((String)->Unit)?):Boolean{
        if(fragmentTagList.size <= 1)
            return false

        fragmentTagList.removeLast()
        currentFragmentTag = fragmentTagList.last()

        currentFragmentChangedObserver?.invoke(currentFragmentTag)

        /* notifying outgoing fragment that its invisible */
        val f1: Fragment? = fragmentManager.findFragmentByTag(currentFragmentTag)
        if (f1 != null && f1 is FragmentLifecycleObserver)
            (f1 as FragmentLifecycleObserver).onFragmentViewInvisible()

        fragmentManager.popBackStack()

        also?.invoke(currentFragmentTag)

        /* notifying current fragment that its visible */
        val f3: Fragment? = fragmentManager.findFragmentByTag(currentFragmentTag)
        if (f3 != null && f3 is FragmentLifecycleObserver)
            (f3 as FragmentLifecycleObserver).onFragmentViewVisible()


        return true
    }

    /**
     * so as a flaw of Fragments, their onStop(), onPause() e.t.c
     * are tied to their HostFragment, so this method attempts to
     * also notify the fragment popped to, that its now visible
     *
     * @param tag KClass#simpleName String of the fragment class you want to pop to,
     *            since FragmentTransaction tags are the classnames for the Fragment involved
     * @return true if backStack was popped or false if current Fragment is the only Fragment in the backstack.
     * also returns false if the tag does not exist in the fragmentManager backStack */
    override fun popBackStack(tag:String):Boolean {
        var wasPopped = false
        val fragmentIndex:Int = fragmentTagList.indexOf(tag)

        if(fragmentTagList.size > 1 && fragmentIndex != -1){

            var currentIndex:Int = fragmentTagList.size - 1
            while(currentIndex != fragmentIndex){
                fragmentTagList.removeLast()
                currentIndex--
            }

            currentFragmentTag = fragmentTagList.last()

            currentFragmentChangedObserver?.invoke(currentFragmentTag)

            /* notifying outgoing fragment that its invisible */
            val f1:Fragment? = fragmentManager.findFragmentByTag(currentFragmentTag)
            if(f1 != null && f1 is FragmentLifecycleObserver)
                (f1 as FragmentLifecycleObserver).onFragmentViewInvisible()

            fragmentManager.popBackStack(tag, 0)

            /* notifying current fragment that its visible */
            val f2:Fragment? = fragmentManager.findFragmentByTag(currentFragmentTag)
            if(f2 != null && f2 is FragmentLifecycleObserver)
                (f2 as FragmentLifecycleObserver).onFragmentViewVisible()

            wasPopped = true
        }

        return wasPopped
    }


    /**
     *
     * pop fragments without affecting your UI per se
     * @param tag the fragment#KClass#simpleName which indicates the fragment to stop
     *            popping at */
    override fun popBackStackInBackground(tag:String): FragmentContinuationStateful {
        isTherePopBackStackInBackground = true
        popBackStackInBackgroundArgs = tag
        return this
    }

    private fun _popBackStackInBackground(tag:String){
        var index:Int = fragmentTagList.indexOf(tag)
        if(++index == 0 || fragmentTagList[index] == null)
            return

        val transaction:FragmentTransaction = fragmentManager.beginTransaction()

        val fragmentTagList_copy:List<String> = listOf(*fragmentTagList.toTypedArray())
        for(i in index until fragmentTagList_copy.size){
            val _tag:String = fragmentTagList_copy[i]
            val f:Fragment? = fragmentManager.findFragmentByTag(_tag)
            if(f == null) {
                continue
            }

            fragmentTagList.remove(f::class.simpleName!!)

            transaction.remove(f)
        }

        currentFragmentTag = fragmentTagList.last()
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
     * this method takes an observer to be notified about changes to visibility
     * for a specific set of transactions
     *
     * @param observer an implementation of the FragmentLifecycleObserver
     * @return the same FragmentContinuationStateful instance
     * */
    override fun setFragmentObserver(observer: FragmentLifecycleObserver): FragmentContinuationStateful {
          isThereSetFragmentObserver = true
          setFragmentObserverArgs = observer
          return this
    }

    private fun _updateFragmentObserver(observer: FragmentLifecycleObserver){
        if(isThereReplace || isThereShow)
            observer.onFragmentViewInvisible()
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
        if(!isThereInto && currentFragmentTag == DEFAULT_STRING_TAG) {
            throw ContainerNotSetException()
        }

        if(isThereInto) {
            _into(intoArgs)
        }

        if(isThereSetCustomAnimations) {
            _setCustomAnimations()
        }

        if(isTherePopBackStackInBackground) {
            _popBackStackInBackground(popBackStackInBackgroundArgs)
        }

        if(isThereReplace) {
            _replace(replaceArgs!!)
        }

        if(isThereHideCurrent) {
            _hideCurrent()
        }

        if(isThereShow) {
            _show(showArgs!!)
        }

        if(isThereSetFragmentObserver) {
            _updateFragmentObserver(setFragmentObserverArgs!!)
        }

        transactionsList.forEach { it.commit() }
        afterArgs?.invoke()

        initialize()
    }
}

