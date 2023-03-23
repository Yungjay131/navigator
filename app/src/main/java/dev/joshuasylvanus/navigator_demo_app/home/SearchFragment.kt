package dev.joshuasylvanus.navigator_demo_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentSearchBinding
import dev.joshuasylvanus.navigator_demo_app.profile.ProfileActivity
import java.lang.reflect.Modifier.PRIVATE

class SearchFragment : Fragment() {
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: FragmentSearchBinding

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
    }

    private fun initViews(){
        binding.btnNext.setOnClickListener {
            Navigator.intentFor<ProfileActivity>(requireActivity())
                .singleTop()
                .navigate()
        }
    }

}