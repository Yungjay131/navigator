package dev.joshuasylvanus.navigator_demo_app.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator.Navigator
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentFavoritesBinding
import dev.joshuasylvanus.navigator_demo_app.profile.ProfileActivity
import java.lang.reflect.Modifier.PRIVATE

class FavoritesFragment : Fragment() {
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: FragmentFavoritesBinding

    companion object {
        @JvmStatic
        fun newInstance(): FavoritesFragment = FavoritesFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
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