package dev.joshuasylvanus.navigator_demo_app.settings

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.BaseActivity
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivitySettingsBinding
import dev.joshuasylvanus.navigator_demo_app.profile.ProfileActivity
import java.lang.reflect.Modifier.PRIVATE

class SettingsActivity : BaseActivity() {
    //region Vars
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding:ActivitySettingsBinding
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnClickListener {
            /* testing that singleTop() is working, it should return
             * the same instance launched from HomeActivity#searchFragment*/
                Navigator.intentFor<ProfileActivity>(this)
                    .singleTop()
                    .navigate()
        }
    }
}