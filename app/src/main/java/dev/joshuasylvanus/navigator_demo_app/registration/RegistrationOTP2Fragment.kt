package dev.joshuasylvanus.navigator_demo_app.registration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.joshuasylvanus.navigator_demo_app.verification.VerificationActivity
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentRegistrationOtp2Binding

class RegistrationOTP2Fragment : Fragment() {
    private lateinit var binding: FragmentRegistrationOtp2Binding

    companion object {
        @JvmStatic
        fun newInstance(): RegistrationOTP2Fragment = RegistrationOTP2Fragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRegistrationOtp2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        Navigator.intentFor<VerificationActivity>(requireActivity())
            .previousIsTop()
            .navigate()

    }
}