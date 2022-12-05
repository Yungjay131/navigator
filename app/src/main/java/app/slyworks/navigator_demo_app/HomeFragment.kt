package app.slyworks.navigator_demo_app

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.VisibleForTesting
import app.slyworks.navigator_demo_app.databinding.FragmentHomeBinding
import java.lang.reflect.Modifier.PRIVATE

class HomeFragment : Fragment() {
    //region Vars
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding:FragmentHomeBinding
    //endregion

    companion object {
        @JvmStatic
        fun newInstance(): HomeFragment = HomeFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


}