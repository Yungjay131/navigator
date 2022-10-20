# navigator
This is a library to handle Activity and Fragment navigation within android apps.

I've used it in a few projects and decided to make it a library.

The entirety of the API methods are mostly static functions on `Navigator`.

There are extension functions for retrieving `Intent extras`. 

Usage
-----
1. Navigating to an Activity in its most basic form.
  ``` Navigator.intentFor<SomeActivity>(this)
               .navigate()
  ```
  where `SomeActivity` is the Activity you want to navigate to and `this` refers to the current Activity's `Context`.
  
2. Navigating to a new Activity, finishing the old Activity.
``` Navigator.intentFor<SomeActivity>(this)
             .finishCaller()
             .navigate()
```

3. Navigating to a new Activity with certain `Intent` flags set (this example is showing all the `Intent` flags for explanation purposes).
``` Navigator.intentFor<SomeActivity>(this)
             .newAndClearTask()
             .clearTop()
             .previousIsTop()
             .singleTop()
             .navigate()
```             
Please note that a new `Intent` flag clears the previously set `Intent` flag (might change this implementation in future releases).             

4. Navigating to a new Activity with `SharedElementTransitions`.
``` Navigator.intentFor<SomeActivity>(this)
             .addSharedElementTransition(textView1, textView1TransitionName)
             .addSharedElementTransition(textView2, textView2TransitionName)
             .newAndClearTask()
             .finishCalller()
             .navigate()
 ```
 `addSharedElementTransition()` takes the `View` and transitionName of `String` as parameters.
 
 5. Navigating to a new Activity also has a similar function (for cases such as being called from Java)
 ``` Navigator.intentFor(this, clazz)
 ```
 where clazz is the `String` className for the Actvity being navigated to.
 
 6. A utility extension function for getting all `Intent` extras from an `Intent`.
 ``` this.intent.getExtra<String>(key, defaultValue)
 ```


Download
--------

```groovy
repositories {
   mavenCentral()
}

dependencies {
   implementation 'app.slyworks.navigator:navigator:1.0.0-alpha'
}
```
