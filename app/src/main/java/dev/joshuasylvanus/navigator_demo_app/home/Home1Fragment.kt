package dev.joshuasylvanus.navigator_demo_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.settings.SettingsActivity
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentHome1Binding
import java.lang.reflect.Modifier.PRIVATE


class Home1Fragment : Fragment() {
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding:FragmentHome1Binding

    companion object {
        @JvmStatic
        fun newInstance(): Home1Fragment = Home1Fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHome1Binding.inflate(inflater, container, false)
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
              .show(Home2Fragment.newInstance())
              .navigate()
        }
    }




}