package app.slyworks.navigator_demo_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity


/**
 * Created by Joshua Sylvanus, 9:32 PM, 17/11/2022.
 */

open class BaseActivity : AppCompatActivity() {

    override fun onDestroy() {
        super.onDestroy()

        ActivityUtils.decrementActivityCount()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityUtils.incrementActivityCount()
    }
}