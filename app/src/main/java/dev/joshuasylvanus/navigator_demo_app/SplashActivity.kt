package dev.joshuasylvanus.navigator_demo_app

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivitySplashBinding
import java.lang.reflect.Modifier.PRIVATE

class SplashActivity : BaseActivity() {
    //region Vars
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: ActivitySplashBinding
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        binding.btnSplash.setOnClickListener {
            Navigator.intentFor<HomeActivity>(this@SplashActivity)
                .newAndClearTask()
                .navigate()
        }
    }

    fun navigateToAppropriateActivity() {}
}