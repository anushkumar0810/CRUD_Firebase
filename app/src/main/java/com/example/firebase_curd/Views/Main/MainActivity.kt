package com.example.firebase_curd.Views.Main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.firebase_curd.MVVM.Model.User
import com.example.firebase_curd.MVVM.ViewModel.AuthViewModel
import com.example.firebase_curd.MVVM.ViewModel.AuthViewModelFactory
import com.example.firebase_curd.Views.AddUserDialog
import com.example.firebase_curd.Views.DeleteDialogFragment
import com.example.firebase_curd.Views.EditDialogFragment
import com.example.firebase_curd.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthViewModel by viewModels { AuthViewModelFactory() }
    private lateinit var userAdapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel.usersList.observe(this, Observer { result ->
            result.onSuccess { users ->
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
                val currentUser = users.firstOrNull { it.userId == currentUserId }

                currentUser?.let {
                    binding.userName.setText(it.name) // Update the displayed name
                }

                userAdapter.submitList(users) // Update the RecyclerView
            }
            result.onFailure {
                // Handle failure (e.g., show a toast or log the error)
            }
        })

        binding.logoutDialog.setOnClickListener{
            finishAffinity()
        }

        binding.add.setOnClickListener{
            addUsersDialog()
        }

        // Set up RecyclerView
        userAdapter = UserAdapter { user, action ->
            when (action) {
                UserAdapter.ACTION_EDIT -> showEditDialog(user)
                UserAdapter.ACTION_DELETE -> showDeleteDialog(user)
            }
        }

        binding.dataRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = userAdapter
        }

        // Observe usersList LiveData
        authViewModel.usersList.observe(this, Observer { result ->
            result.onSuccess { users ->
                userAdapter.submitList(users) // Update the RecyclerView
            }
            result.onFailure { exception ->
                // Handle failure (e.g., show a toast or log the error)
            }
        })

        // Set SwipeRefreshLayout listener
        binding.swiperefresh.setOnRefreshListener(this)

        // Fetch all users except the logged-in user
        authViewModel.getAllUsersExceptLoggedIn()
    }

    private fun showEditDialog(user: User) {
        val dialog = EditDialogFragment.newInstance(user)
        dialog.setOnSaveClickListener { updatedUser ->
            authViewModel.updateUser(updatedUser)
        }
        dialog.setOnDialogCloseListener {
            onRefresh() // Call refresh when dialog is closed
        }
        dialog.show(supportFragmentManager, "EditDialog")
    }

    private fun showDeleteDialog(user: User) {
        val dialog = DeleteDialogFragment.newInstance(user)
        dialog.setOnDeleteClickListener {
            authViewModel.deleteUser(user.userId)
        }
        dialog.setOnDialogCloseListener {
            onRefresh() // Call refresh when dialog is closed
        }
        dialog.show(supportFragmentManager, "DeleteDialog")
    }

    private fun addUsersDialog() {
        val dialog = AddUserDialog()

        dialog.setOnSaveClickListener { name, email, password ->
            val user = User(name = name, email = email, password = password)
            authViewModel.signUp(user)
        }

        dialog.setOnDialogCloseListener {
            onRefresh() // Call refresh when dialog is closed
        }
        // Show the dialog
        dialog.show(supportFragmentManager, "AddUserDialog")
    }


    override fun onRefresh() {
        binding.swiperefresh.isRefreshing = true
        authViewModel.getAllUsersExceptLoggedIn()
        authViewModel.usersList.observe(this, Observer { result ->
            result.onSuccess { users ->
                userAdapter.submitList(users)
                binding.swiperefresh.isRefreshing = false
            }
            result.onFailure {
                binding.swiperefresh.isRefreshing = false
            }
        })
    }

}
