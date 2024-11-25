package com.example.tourtailor.ui.auth

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun register(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null)
            } else {
                onComplete(false, task.exception?.message)
            }
        }
    }

    fun login(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null)
            } else {
                onComplete(false, task.exception?.message)
            }
        }
    }

    fun resetPassword(email: String, onComplete: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onComplete(true, null)
            } else {
                onComplete(false, task.exception?.message)
            }
        }
    }

    fun logout(onComplete: () -> Unit) {
        auth.signOut()
        onComplete()
    }

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null
    }
}
