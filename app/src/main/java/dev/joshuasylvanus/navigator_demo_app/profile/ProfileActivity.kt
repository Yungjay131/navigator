package dev.joshuasylvanus.navigator_demo_app.profile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator.interfaces.FragmentContinuationStateful
import dev.joshuasylvanus.navigator_demo_app.BaseActivity
import dev.joshuasylvanus.navigator_demo_app.MOnBackPressedCallback
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivityProfileBinding

class ProfileActivity : BaseActivity() {
    lateinit var navigator:FragmentContinuationStateful
    private lateinit var binding:ActivityProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        navigator = Navigator.transactionWithStateFrom(supportFragmentManager)

        this.onBackPressedDispatcher
            .addCallback(this, MOnBackPressedCallback(this, navigator))

     navigator
         .into(binding.fragmentContainerProfile.id)
         .show(ProfileFragment.newInstance())
         .navigate()
    }
}