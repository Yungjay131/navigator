package dev.joshuasylvanus.navigator_demo_app.splash

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.Navigator.Companion.getExtra
import dev.joshuasylvanus.navigator_demo_app.BaseActivity
import dev.joshuasylvanus.navigator_demo_app.KEY_SPLASH_ARGS
import dev.joshuasylvanus.navigator_demo_app.MOnBackPressedCallback
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivitySplashBinding
import dev.joshuasylvanus.navigator_demo_app.login.LoginActivity
import dev.joshuasylvanus.navigator_demo_app.onboarding.OnboardingActivity
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
        initData()
    }

    private fun initViews(){
        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this))

        binding.btnNext.setOnClickListener {
            Navigator.intentFor<OnboardingActivity>(this@SplashActivity)
                .newAndClearTask()
                .navigate()
        }
    }

    private fun initData(){
        val restartDestination:String? = intent.getExtra(KEY_SPLASH_ARGS)
        if(restartDestination != null)
           Navigator.intentFor<LoginActivity>(this)
               .newAndClearTask()
               .navigate()
    }

    fun navigateToAppropriateActivity() {}
}