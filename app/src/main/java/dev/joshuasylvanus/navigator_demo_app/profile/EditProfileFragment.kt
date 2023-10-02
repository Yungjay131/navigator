package dev.joshuasylvanus.navigator_demo_app.profile

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.KEY_SPLASH_ARGS
import dev.joshuasylvanus.navigator_demo_app.LOGIN_ACTIVITY
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {
    private lateinit var binding:FragmentEditProfileBinding

    companion object {
        @JvmStatic
        fun newInstance(): EditProfileFragment = EditProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnClickListener {
            Navigator.minimizeApp(requireActivity())
        }

        binding.btnNext2.setOnClickListener{
            Navigator.restartApp(requireActivity(),mapOf(KEY_SPLASH_ARGS to LOGIN_ACTIVITY))
        }

        binding.btnNext.setOnLongClickListener{
            Navigator.restartApp(requireActivity())
            true
        }
    }

}