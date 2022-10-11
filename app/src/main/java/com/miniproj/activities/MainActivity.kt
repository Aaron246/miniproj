package com.miniproj.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproj.databinding.ActivityMainBinding
import com.miniproj.model.User
import com.miniproj.utils.Constants

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fUser = FirebaseAuth.getInstance().currentUser?.uid

        if (fUser != null) {
            autoSignIn()
        }

        binding.btnSignIn.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignInActivity::class.java))
        }

        binding.btnSignUp.setOnClickListener {
            startActivity(Intent(this@MainActivity, SignUpActivity::class.java))
        }
    }

    private fun autoSignIn(){
            showProgressDialog("Please Wait")
            FirebaseFirestore.getInstance().collection(Constants.USERS).document(getCurrentUserID()).get()
                .addOnSuccessListener {
                        document ->
                    val user = document.toObject(User::class.java)!!
                    if(user.accountType == Constants.DOCTOR){
                        hideProgressDialog()
                        startActivity(Intent(this, DoctorActivity::class.java))
                        finish()
                    }
                    if(user.accountType == Constants.PATIENT){
                        hideProgressDialog()
                        startActivity(Intent(this, PatientActivity::class.java))
                        finish()
                    }
                }.addOnFailureListener {
                    showToast("auto sign in failed")
                }
    }
}