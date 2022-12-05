package app.slyworks.navigator_demo_app

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.DialogFragment
import app.slyworks.navigator_demo_app.databinding.DialogExitBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.reflect.Modifier.PRIVATE
import kotlin.system.exitProcess


/**
 *Created by Joshua Sylvanus, 9:11 PM, 16/11/2022.
 */
class ExitDialog : DialogFragment() {
    //region Vars
    @VisibleForTesting(otherwise = PRIVATE)
    lateinit var binding: DialogExitBinding
    //endregion

    companion object{
        @JvmStatic
        fun newInstance(): ExitDialog = ExitDialog()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireContext(), theme).apply {
            val dialogView = onCreateView(LayoutInflater.from(requireContext()),null, savedInstanceState)
            dialogView?.let {
                onViewCreated(it,savedInstanceState)
            }
            setView(dialogView)
        }.create()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DialogExitBinding.inflate(inflater, container, false)
        initViews(binding.root)
        return binding.root
    }

    private fun initViews(view:View){
        binding.tvCancelDialogExit.setOnClickListener { dismiss() }
        binding.tvExitDialogExit.setOnClickListener { exitProcess(0) }
    }
}