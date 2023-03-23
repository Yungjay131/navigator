package dev.joshuasylvanus.navigator_demo_app.onboarding

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.BaseActivity
import dev.joshuasylvanus.navigator_demo_app.FRAGMENT_REG_OTP
import dev.joshuasylvanus.navigator_demo_app.KEY_FRAGMENT
import dev.joshuasylvanus.navigator_demo_app.login.LoginActivity
import dev.joshuasylvanus.navigator_demo_app.MOnBackPressedCallback
import dev.joshuasylvanus.navigator_demo_app.registration.RegistrationActivity
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityOnboardingBinding
import java.lang.reflect.Modifier.PRIVATE

class OnboardingActivity : BaseActivity() {

    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding:ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this))

        binding.btnGetStarted.setOnClickListener {
            Navigator.intentFor<RegistrationActivity>(this@OnboardingActivity)
                .navigate()
        }

        binding.btnGetStarted.setOnLongClickListener {
            Navigator.intentFor<RegistrationActivity>(this@OnboardingActivity)
                .addExtra(KEY_FRAGMENT, FRAGMENT_REG_OTP)
                .navigate()
            true
        }

        binding.btnLogin.setOnClickListener {
            Navigator.intentFor<LoginActivity>(this@OnboardingActivity)
                .navigate()
        }
    }
}