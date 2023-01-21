# navigator
This is a library to handle Activity and Fragment navigation within android apps.

I've used it in a few projects and decided to make it a library.

The entirety of the API methods are mostly static functions on `Navigator`.

There are extension functions for retrieving `Intent extras`. 

Usage
-----
1. Navigating to an Activity in its most basic form.
  ```kotlin
  Navigator.intentFor<SomeActivity>(this)
               .navigate()
  ```
  where `SomeActivity` is the Activity you want to navigate to and `this` refers to the current Activity's `Context`.
  
  
  
2. Navigating to a new Activity, finishing the old Activity.
```kotlin
Navigator.intentFor<SomeActivity>(this)
             .finishCaller()
             .navigate()
```


3. Navigating to a new Activity with certain `Intent` flags set (this example is showing all the `Intent` flags for explanation purposes).
```kotlin
Navigator.intentFor<SomeActivity>(this)
             .newAndClearTask()
             .clearTop()
             .previousIsTop()
             .singleTop()
             .noHistory()
             .reorderToFront()
             .navigate()
```             
Please note that a new `Intent` flag adds to the previously set `Intent` flag (might change this implementation in future releases).


4. Navigating to a new Activity with `SharedElementTransitions`.
```kotlin
Navigator.intentFor<SomeActivity>(this)
             .addSharedElementTransition(textView1, textView1TransitionName)
             .addSharedElementTransition(textView2, textView2TransitionName)
             .newAndClearTask()
             .finishCalller()
             .navigate()
 ```
 `addSharedElementTransition()` takes the `View` and transitionName of `String` as parameters.
 
 
 5. Navigating to a new Activity also has a similar function (for cases such as being called from Java)
 ``` kotlin
 Navigator.intentFor(this, clazz)
 ```
 where clazz is the `String` className for the Actvity being navigated to.
 
6. Navigating to activities using implicit intents, which is useful in multi-module projects where an activity may not "know" about another activity
```kotlin
Navigator.IntentFor(this, SOME_ACTIVITY_INTENT_FILTER)
```

7. A utility extension function for getting all `Intent` extras from an `Intent`.
 ``` kotlin
 this.intent.getExtra<String>(key, defaultValue)
 ```
 
Navigator also provides functions for handling fragment transaction operations, which comes in 2 variants

a - fire and forget fragment transactions
```kotlin
Navigator.transactionFrom(supportFragmentManager) 
         .into(R.id.fraggment_container)
         .show(ExampleFragment.newInstance())
         .navigate()
```


b - Fragment transactions where state is required

b - 
```kotlin
lateinit val fragmentNavigator:FragmentContinuationStateful

fragmentNavigator = Navigator.transactionWithStateFrom(supportFragmentManager)
   .into(R.id.fragmentContainer)
   .hideCurrent()
   .show(ExampleFragment.newInstance())
   .navigate()
```
please note:
A ContainerAlreadySetException is thrown if you try to set the fragment container id more than once

subsequent usages would be 
```kotlin
fragmentNavigator
    .hideCurrent()
    .show()
    .navigate()
```

to replace an existing fragment
```kotlin 
fragmentNavigator
   .replace()
   .navigate()
```

A common pain point of working with Fragments is handling the back navigation properly
navigator handles this by
```kotlin
  val wasFragmentPopped:Boolean = fragmentNavigator.popBackstack()
```
 
practical usage of this could be seen below
```kotlin
this.onBackPressedDispatcher
            .addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() { 
               if(!fragmentNavigator!!.popBackStack())
                //means currently visible fragment is the last fragment in the activity 
                activity.finish()
                }
            })
 ```           
            
            

Download
--------

```groovy
repositories {
   mavenCentral()
}

dependencies {
   implementation 'dev.joshuasylvanus.navigator:navigator:1.0.0-alpha'
}
```
