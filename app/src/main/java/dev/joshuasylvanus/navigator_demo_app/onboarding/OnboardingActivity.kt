package dev.joshuasylvanus.navigator_demo_app.onboarding

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.*
import dev.joshuasylvanus.navigator_demo_app.login.LoginActivity
import dev.joshuasylvanus.navigator_demo_app.registration.RegistrationActivity
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityOnboardingBinding
import dev.joshuasylvanus.navigator_demo_app.splash.Args
import java.lang.reflect.Modifier.PRIVATE

class OnboardingActivity : BaseActivity() {
    private val TAG: String? = OnboardingActivity::class.simpleName

    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding:ActivityOnboardingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        val parcelableList:List<Args> = intent.getParcelableArrayListExtra<Args>(KEY_OB_ARGS_1)!!
        val intList:List<Int> = intent.getIntegerArrayListExtra(KEY_OB_ARGS_2)!!
        val stringsList:List<CharSequence> = intent.getCharSequenceArrayListExtra(KEY_OB_ARGS_3)!!

        Log.e(TAG,"parcelableList:$parcelableList")
        Log.e(TAG,"intList:$intList")
        Log.e(TAG,"stringsList:$stringsList")

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