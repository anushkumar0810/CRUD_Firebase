package com.example.firebase_curd.Views

import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.firebase_curd.MVVM.Model.User
import com.example.firebase_curd.R
import com.example.firebase_curd.databinding.FragmentDeleteDialogBinding


class DeleteDialogFragment : DialogFragment() {

    private var user: User? = null
    private var onDeleteClickListener: (() -> Unit)? = null
    private var onDialogCloseListener: (() -> Unit)? = null

    companion object {
        fun newInstance(user: User): DeleteDialogFragment {
            val fragment = DeleteDialogFragment()
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
        onDialogCloseListener?.invoke() // Notify when dialog is dismissed
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentDeleteDialogBinding.inflate(inflater, container, false)
        user = arguments?.getParcelable("user")

        binding.yesButton.setOnClickListener {
            onDeleteClickListener?.invoke() // Perform delete action
            dismiss() // Dismiss the dialog
        }

        binding.noButton.setOnClickListener {
            dismiss() // Simply dismiss the dialog
        }

        return binding.root
    }

    fun setOnDeleteClickListener(listener: () -> Unit) {
        onDeleteClickListener = listener
    }
}

