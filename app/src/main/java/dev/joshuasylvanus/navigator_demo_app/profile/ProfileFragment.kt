package dev.joshuasylvanus.navigator_demo_app.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentProfileBinding

class ProfileFragment : Fragment() {

    private lateinit var binding:FragmentProfileBinding

    companion object {
        @JvmStatic
        fun newInstance(): ProfileFragment = ProfileFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnClickListener {
            (requireActivity() as ProfileActivity).navigator
                .replace(EditProfileFragment.newInstance())
                .navigate()
        }
    }
}