package com.example.firebase_curd.MVVM.Repository

import com.example.firebase_curd.MVVM.Model.User
import com.example.firebase_curd.helper.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    suspend fun signUp(user: User): Result<String> {
        return try {
            val result = auth.createUserWithEmailAndPassword(user.email, user.password).await()
            val userId = result.user?.uid ?: return Result.failure(Exception("User ID is null"))
            user.copy(userId = userId).also {
                db.collection(Constants.COLLECTION_USERS).document(userId).set(it).await()
            }
            Result.success("Sign-up successful!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun login(email: String, password: String): Result<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val userId = result.user?.uid ?: return Result.failure(Exception("User ID is null"))
            val document = db.collection(Constants.COLLECTION_USERS).document(userId).get().await()
            if (document.exists()) {
                val user = document.toObject(User::class.java)!!
                Result.success(user)
            } else {
                Result.failure(Exception("User data not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getAllUsersExceptLoggedIn(): Result<List<User>> {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val userId = currentUser?.uid ?: return Result.failure(Exception("No logged-in user"))

            val usersSnapshot = db.collection(Constants.COLLECTION_USERS).get().await()
            val users = usersSnapshot.documents
                .filter { it.id != userId }  // Exclude the logged-in user
                .mapNotNull { it.toObject(User::class.java) }

            Result.success(users)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Method to update user data in Firestore
    suspend fun updateUser(user: User): Result<String> {
        return try {
            db.collection(Constants.COLLECTION_USERS).document(user.userId).set(user).await()
            Result.success("User updated successfully!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Method to delete user from Firestore
    suspend fun deleteUser(userId: String): Result<String> {
        return try {
            db.collection(Constants.COLLECTION_USERS).document(userId).delete().await()
            Result.success("User deleted successfully!")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}