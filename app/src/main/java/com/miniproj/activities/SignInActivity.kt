package com.miniproj.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.miniproj.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.miniproj.R
import com.miniproj.databinding.ActivitySignInBinding
import com.miniproj.model.User
import com.miniproj.utils.Constants

class SignInActivity : BaseActivity() {

    private lateinit var binding: ActivitySignInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()
        binding.btnSignIn.setOnClickListener {
            signInUser()
        }
    }

    private fun signInUser() {

        val email = binding.etEmail.text.toString().trim{it<=' '}
        val password = binding.etPassword.text.toString().trim{it<=' '}

        if(validateForm(email,password)) {
            showProgressDialog("Please Wait")
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnSuccessListener {
                FirestoreClass().signInUser(this)
            }.addOnFailureListener {
                hideProgressDialog()
                showToast("sign in failed")
            }
        }
    }

    private fun validateForm(email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show()
                false
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSignInActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarSignInActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun signInSuccess(user: User) {
        hideProgressDialog()
        if(user.accountType == Constants.DOCTOR) {
            val intent = Intent(this, DoctorActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        if(user.accountType == Constants.PATIENT) {
            val intent = Intent(this, PatientActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }
        finish()
    }
}