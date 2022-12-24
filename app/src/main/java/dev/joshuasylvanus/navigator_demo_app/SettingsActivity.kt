package dev.joshuasylvanus.navigator_demo_app

import android.os.Bundle
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator_demo_app.databinding.ActivitySettingsBinding
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
    }
}