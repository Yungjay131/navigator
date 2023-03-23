package dev.joshuasylvanus.navigator_demo_app.verification

import android.os.Bundle
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.*
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityVerificationBinding
import dev.joshuasylvanus.navigator_demo_app.home.HomeActivity


class VerificationActivity : BaseActivity() {
    private lateinit var binding: ActivityVerificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this))

        binding.ivBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            Navigator.intentFor<HomeActivity>(this)
                .addExtra(KEY_HOME_ARGS, REGISTRATION_ACTIVITY)
                .newAndClearTask()
                .navigate()
        }
    }

}