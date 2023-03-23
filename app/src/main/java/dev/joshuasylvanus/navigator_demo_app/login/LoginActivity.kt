package dev.joshuasylvanus.navigator_demo_app.login

import android.os.Bundle
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.*
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityLoginBinding
import dev.joshuasylvanus.navigator_demo_app.home.HomeActivity

class LoginActivity : BaseActivity() {
    private lateinit var binding:ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this))

        binding.ivBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            Navigator.intentFor<HomeActivity>(this)
                .addExtra(KEY_HOME_ARGS, LOGIN_ACTIVITY)
                .newAndClearTask()
                .navigate()
        }
    }
}