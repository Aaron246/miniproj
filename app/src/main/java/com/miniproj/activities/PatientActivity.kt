package com.miniproj.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproj.databinding.ActivityPatientBinding
import com.miniproj.model.User
import com.miniproj.utils.Constants

class PatientActivity : BaseActivity() {
    private lateinit var binding: ActivityPatientBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPatientBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setData()



        binding.btnPatientLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    fun setData(){
        FirebaseFirestore.getInstance().collection(Constants.USERS).document(getCurrentUserID())
            .get().addOnSuccessListener { document->
                val user = document.toObject(User::class.java)!!
                /*binding.uidPatient.text = user.id
                binding.namePatient.text = user.name
                binding.typePatient.text = user.accountType
                binding.emailPatient.text = user.email*/
            }.addOnFailureListener {
                showToast("get data failed")
            }
    }
}