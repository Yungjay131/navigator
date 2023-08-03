package dev.joshuasylvanus.navigator_demo_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.Navigator.Companion.getExtra
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import dev.joshuasylvanus.navigator_demo_app.*
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityRegistrationBinding

class RegistrationActivity : BaseActivity() {

    private val fragmentMap:Map<String, () -> Fragment> = mapOf(
        FRAGMENT_REG_ZERO to RegistrationGeneral0Fragment::newInstance,
        FRAGMENT_REG_ONE to RegistrationGeneral1Fragment::newInstance,
        FRAGMENT_REG_TWO to RegistrationGeneral2Fragment::newInstance,
        FRAGMENT_REG_OTP to RegistrationOTP1Fragment::newInstance )

    private lateinit var binding: ActivityRegistrationBinding

    lateinit var navigator: FragmentContinuationStateful

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }


    private fun initViews(){
        navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this, navigator))

        val fragKey:String = intent.getExtra<String>(KEY_FRAGMENT, FRAGMENT_REG_ONE)!!

        navigator
            .into(binding.rootView.id)
            .show(fragmentMap[fragKey]!!())
            .navigate()
    }


}