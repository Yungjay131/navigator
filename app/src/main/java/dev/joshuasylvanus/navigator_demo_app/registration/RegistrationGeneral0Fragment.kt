package dev.joshuasylvanus.navigator_demo_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentRegistrationGeneral0Binding

class RegistrationGeneral0Fragment : Fragment() {
    private lateinit var binding: FragmentRegistrationGeneral0Binding

    companion object {
        @JvmStatic
        fun newInstance(): RegistrationGeneral0Fragment = RegistrationGeneral0Fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
       binding = FragmentRegistrationGeneral0Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnClickListener {
            (requireActivity() as RegistrationActivity)
                .navigator
                .show(RegistrationGeneral1Fragment.newInstance())
                .navigate()
        }
    }
}