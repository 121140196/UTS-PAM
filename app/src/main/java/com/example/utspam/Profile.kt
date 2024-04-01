package com.example.utspam

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class Profile : Fragment() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var buttonLogout: Button
    private lateinit var imageProfile: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        db = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()

        buttonLogout = view.findViewById(R.id.buttonLogout)
        imageProfile = view.findViewById(R.id.imageProfile)
        nameTextView = view.findViewById(R.id.nameTextView)
        usernameTextView = view.findViewById(R.id.usernameTextView)
        emailTextView = view.findViewById(R.id.emailTextView)


        buttonLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }

        val currentUser = firebaseAuth.currentUser
        currentUser?.uid?.let { userId ->
            db.collection("users").document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val name = document.getString("name") ?: "Default Name"
                        val email = document.getString("email") ?: "Default Email"
                        val username = document.getString("username") ?: "Default Username"

                        nameTextView.text = name
                        emailTextView.text = email
                        usernameTextView.text = username

                        val nameParts = name.split(" ")
                        val initials = if (nameParts.isNotEmpty()) {
                            nameParts[0].firstOrNull()?.toString() ?: ""
                        } else {
                            ""
                        }
                        imageProfile.text = initials

                    } else {
                        nameTextView.text = "Data not found"
                    }
                }
                .addOnFailureListener { exception ->
                    nameTextView.text = "Error: ${exception.message}"
                }
        }
        return view
    }
}