 package dev.joshuasylvanus.navigator_demo_app.registration

import android.Manifest
import android.app.DatePickerDialog
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import dev.joshuasylvanus.navigator_demo_app.databinding.FragmentRegistrationGeneral2Binding
import java.text.SimpleDateFormat
import java.util.*

 class RegistrationGeneral2Fragment : Fragment() {
    private lateinit var binding: FragmentRegistrationGeneral2Binding

    companion object {
        @JvmStatic
        fun newInstance(): RegistrationGeneral2Fragment = RegistrationGeneral2Fragment()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentRegistrationGeneral2Binding.inflate(layoutInflater, container, false)
        return binding.root
    }

     override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
         super.onViewCreated(view, savedInstanceState)

         initViews()
     }

     private fun initViews(){
        binding.btnNext.setOnClickListener {
            (requireActivity() as RegistrationActivity).navigator
                .show(RegistrationOTP1Fragment.newInstance())
                .navigate()
        }
     }
}