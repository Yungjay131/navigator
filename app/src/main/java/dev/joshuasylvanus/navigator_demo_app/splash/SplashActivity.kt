package dev.joshuasylvanus.navigator_demo_app.splash

import android.os.Bundle
import android.os.Parcelable
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.Navigator.Companion.getExtra
import dev.joshuasylvanus.navigator_demo_app.*
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

        val parcelableList:List<Args> = listOf(Args("1"), Args(""))
        val stringsList:List<String> = listOf("1","2")
        val intList:List<Int> = listOf(1,2)

        binding.btnNext.setOnClickListener {
            Navigator.intentFor<OnboardingActivity>(this@SplashActivity)
                .addExtra(KEY_OB_ARGS_1, parcelableList)
                .addExtra(KEY_OB_ARGS_2, stringsList)
                .addExtra(KEY_OB_ARGS_3, intList)
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