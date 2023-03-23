package dev.joshuasylvanus.navigator_demo_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.settings.SettingsActivity
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentHome0Binding
import java.lang.reflect.Modifier.PRIVATE


class Home0Fragment : Fragment() {
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding:FragmentHome0Binding

    companion object {
        @JvmStatic
        fun newInstance(): Home0Fragment = Home0Fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHome0Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnLongClickListener() {
            Navigator.intentFor<SettingsActivity>(requireActivity())
                .noHistory()
                .navigate()

            true
        }

        binding.btnNext.setOnClickListener {
            (requireActivity() as HomeActivity).navigator
                .show(Home1Fragment.newInstance())
                .navigate()
        }
    }




}