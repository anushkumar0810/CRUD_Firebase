package com.example.firebase_curd.Views

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.firebase_curd.R
import com.example.firebase_curd.databinding.FragmentAddUserDialogBinding

class AddUserDialog : DialogFragment() {

    private var onSaveClickListener: ((String, String, String) -> Unit)? = null

    private lateinit var binding: FragmentAddUserDialogBinding
    private var onDialogCloseListener: (() -> Unit)? = null

    fun setOnDialogCloseListener(listener: () -> Unit) {
        onDialogCloseListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogCloseListener?.invoke() // Notify when dialog is dismissed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddUserDialogBinding.inflate(inflater, container, false)

        // Set up the Save button click listener
        binding.saveButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (name.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                // Pass data back using the listener
                onSaveClickListener?.invoke(name, email, password)
                dismiss() // Close the dialog
            } else {
                // Show error message if any field is empty
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Adjust the width of the dialog
        val dialogWidth = 0.85  // 85% of screen width (you can adjust this value)
        val displayMetrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = (displayMetrics.widthPixels * dialogWidth).toInt()

        // Set the width of the dialog
        dialog?.window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    fun setOnSaveClickListener(listener: (String, String, String) -> Unit) {
        onSaveClickListener = listener
    }
}
