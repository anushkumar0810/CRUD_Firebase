package com.example.firebase_curd.Views

import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.firebase_curd.MVVM.Model.User
import com.example.firebase_curd.databinding.FragmentEditDialogBinding

class EditDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentEditDialogBinding
    private var user: User? = null
    private var onSaveClickListener: ((User) -> Unit)? = null
    private var onDialogCloseListener: (() -> Unit)? = null

    companion object {
        fun newInstance(user: User): EditDialogFragment {
            val fragment = EditDialogFragment()
            val args = Bundle()
            args.putParcelable("user", user)
            fragment.arguments = args
            return fragment
        }
    }

    fun setOnDialogCloseListener(listener: () -> Unit) {
        onDialogCloseListener = listener
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogCloseListener?.invoke()  // Notify when dialog is dismissed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDialogBinding.inflate(inflater, container, false)

        user = arguments?.getParcelable("user")

        binding.editName.setText(user?.name)
        binding.editEmail.setText(user?.email)

        binding.saveButton.setOnClickListener {
            val updatedUser = user?.copy(
                name = binding.editName.text.toString(),
                email = binding.editEmail.text.toString()
            )
            updatedUser?.let {
                onSaveClickListener?.invoke(it)  // Save the updated user
                dismiss()  // Dismiss the dialog
                onDialogCloseListener?.invoke()  // Notify when dialog is dismissed
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

    fun setOnSaveClickListener(listener: (User) -> Unit) {
        onSaveClickListener = listener
    }
}

