package com.miniproj.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.miniproj.R
import com.miniproj.databinding.ActivityDoctorBinding
import com.miniproj.model.User
import com.miniproj.utils.Constants

class DoctorActivity : BaseActivity() {
    private lateinit var binding: ActivityDoctorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //setData()

        binding.btnDoctorLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        binding.btnMyPatients.setOnClickListener {
            startActivity(Intent(this, MyPatientsActivity::class.java))
        }
    }

    fun setData(){
        FirebaseFirestore.getInstance().collection(Constants.USERS).document(getCurrentUserID())
            .get().addOnSuccessListener { document->
                val user = document.toObject(User::class.java)!!
                /*binding.uidDoctor.text = user.id
                binding.nameDoctor.text = user.name
                binding.typeDoctor.text = user.accountType
                binding.emailDoctor.text = user.email*/
            }.addOnFailureListener {
                showToast("get data failed")
            }}
}