package dev.joshuasylvanus.navigator_demo_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentRegistrationGeneral1Binding

class RegistrationGeneral1Fragment : Fragment() {
    private lateinit var binding: FragmentRegistrationGeneral1Binding

    companion object {
        @JvmStatic
        fun newInstance(): RegistrationGeneral1Fragment = RegistrationGeneral1Fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegistrationGeneral1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnClickListener {
            (requireActivity() as RegistrationActivity).navigator
                .show(RegistrationGeneral2Fragment.newInstance())
                .navigate()
        }
    }

}