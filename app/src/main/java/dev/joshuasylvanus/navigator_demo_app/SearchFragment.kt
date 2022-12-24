package dev.joshuasylvanus.navigator_demo_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentSearchBinding
import java.lang.reflect.Modifier.PRIVATE

class SearchFragment : Fragment() {
    //region Vars
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: FragmentSearchBinding
    //endregion

    companion object {
        @JvmStatic
        fun newInstance(): SearchFragment = SearchFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}