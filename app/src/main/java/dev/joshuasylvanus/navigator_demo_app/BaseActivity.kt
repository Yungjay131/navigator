package dev.joshuasylvanus.navigator_demo_app

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by Joshua Sylvanus, 9:32 PM, 17/11/2022.
 */
const val KEY_ACTIVITY_COUNT = "key_activity_count"

open class BaseActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()

        ActivityUtils.decrementActivityCount()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(savedInstanceState != null){
            ActivityUtils.setActivityCount(savedInstanceState.getInt(KEY_ACTIVITY_COUNT) - 1)
        }

        ActivityUtils.incrementActivityCount()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_ACTIVITY_COUNT, ActivityUtils.getActivityCount())
    }

}