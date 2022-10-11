package com.miniproj.activities

import android.os.Bundle
import android.text.TextUtils
import com.miniproj.firebase.FirestoreClass
import com.google.firebase.auth.FirebaseAuth
import com.miniproj.R
import com.miniproj.databinding.ActivitySignUpBinding
import com.miniproj.model.User
import com.miniproj.utils.Constants

class SignUpActivity : BaseActivity() {

    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        binding.cbDoctor.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                binding.cbPatient.isChecked = false
            }
        }

        binding.cbPatient.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                binding.cbDoctor.isChecked = false
            }
        }

        binding.btnSignUp.setOnClickListener {
            signUpUser()
        }

    }

    private fun signUpUser() {
        val name = binding.etName.text.toString().trim{it<=' '}
        val email = binding.etEmail.text.toString().trim{it<=' '}
        val password = binding.etPassword.text.toString().trim{it<=' '}
        var accountType = ""

        if(binding.cbDoctor.isChecked){
            accountType = Constants.DOCTOR
        }

        if(binding.cbPatient.isChecked){
            accountType = Constants.PATIENT
        }

        if(validateForm(name, email, password)){
            showProgressDialog("Please Wait")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener{
                task ->
                val firebaseUser = task.user!!
                val user = User(
                    firebaseUser.uid, name, email, "", 0, "", accountType
                )
                FirestoreClass().signUpUser(this, user)
            }.addOnFailureListener {
                hideProgressDialog()
                showErrorSnackBar("sign up failed")
            }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter name")
                false
            }
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter password")
                false
            }
            !(binding.cbPatient.isChecked || binding.cbDoctor.isChecked) -> {
                showErrorSnackBar("Please select the account type")
                false
            }
            else -> {
                true
            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarSignUpActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        binding.toolbarSignUpActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun signUpSuccess() {
        hideProgressDialog()
        showToast("account created successfully, you can now login")
        FirebaseAuth.getInstance().signOut()
        finish()
    }
}
