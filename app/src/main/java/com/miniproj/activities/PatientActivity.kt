package com.miniproj.activities

import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproj.R
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
        binding.btnShowRecord.setOnClickListener {
            val showUIDialog = Dialog(this, R.style.ThemeOverlay_AppCompat_Dialog)
            showUIDialog.setContentView(R.layout.dialog_enter_uid)
            showUIDialog.findViewById<AppCompatButton>(R.id.btn_show_record_dialog).setOnClickListener {
                val UID = showUIDialog.findViewById<EditText>(R.id.et_dialog_enter_UID).text.toString()
                val intent = Intent(this, DetailsActivity::class.java)
                intent.putExtra("UID", UID)
                startActivity(intent)
            }
        }



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