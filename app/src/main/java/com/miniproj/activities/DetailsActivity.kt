package com.miniproj.activities

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.text.AlphabeticIndex
import android.os.Bundle
import android.util.Log
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.miniproj.databinding.ActivityDetailsBinding
import com.miniproj.firebase.FirestoreClass
import com.miniproj.model.RecordBlock
import com.miniproj.model.User
import com.miniproj.utils.Constants
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class DetailsActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*val UID = intent.getStringExtra("UID")!!
        if (UID.isNotEmpty()){
            FirebaseFirestore.getInstance().collection(Constants.DETAILS).document(getCurrentUserID()).collection(textpatient).document(textdate)
            .get().addOnSuccessListener {
                if(it.exists()){
                    val block = it.toObject(RecordBlock::class.java)!!
                    binding.etName.setText(block.name)
                    binding.etAge.setText(block.age.toString())
                    binding.etDiagnosis.setText(block.diagnosis)
                    binding.etPrescribedMeds.setText(block.prescribedMeds)
                    binding.etNextDate.setText(block.nextDate)
                    if(block.gender=="male"){
                        binding.cbMale.isChecked = true
                    }else{
                        binding.cbFemale.isChecked = false
                    }
                }
            }.addOnFailureListener { showToast("failed") }
        }*/
        checkBoxLogic()
        checkData()
        val textdate = intent.getStringExtra("date")
        val textpatient = intent.getStringExtra("patient")
        binding.tvDateOfVisit.text = textdate
        binding.etName.setText(textpatient)
        FirebaseFirestore.getInstance().collection(Constants.USERS).document(getCurrentUserID())
            .get().addOnSuccessListener { document->
                val user = document.toObject(User::class.java)!!
                binding.tvDoctorName.text = user.name
            }.addOnFailureListener {
                showToast("get data failed")
            }
        binding.etNextDate.setOnClickListener {
            val cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMM yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                setDate(sdf.format(cal.time))
            }
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
        binding.btnSave.setOnClickListener {
            saveDetails()
            finish()
        }
        binding.btnShare.setOnClickListener {
            var gender = ""
            if(binding.cbMale.isChecked){
                gender = "male"
            }

            if(binding.cbFemale.isChecked){
                gender = "female"
            }
            val send: String =
                    "Doctor's Name: "+binding.tvDoctorName.text.toString()+
                    "\nDate of visit: "+binding.tvDateOfVisit.text.toString()+
                    "\nPatient's Name: "+binding.etName.text.toString()+
                    "\nAge: "+parseInt().toString()+
                    "\nGender: "+gender+
                    "\nDiagnosis: "+binding.etDiagnosis.text.toString()+
                    "\nPrescribed Medicines: \n"+binding.etPrescribedMeds.text.toString()+
                    "\nNext date of visit: "+binding.etNextDate.text.toString()
            val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, send)
            type = "text/plain"
        }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun checkData() {
        val textpatient = intent.getStringExtra("patient")!!
        val textdate = intent.getStringExtra("date")!!
        FirebaseFirestore.getInstance().collection(Constants.DETAILS).document(getCurrentUserID()).collection(textpatient).document(textdate)
            .get().addOnSuccessListener {
                if(it.exists()){
                    val block = it.toObject(RecordBlock::class.java)!!
                    binding.etName.setText(block.name)
                    binding.etAge.setText(block.age.toString())
                    binding.etDiagnosis.setText(block.diagnosis)
                    binding.etPrescribedMeds.setText(block.prescribedMeds)
                    binding.etNextDate.setText(block.nextDate)
                    if(block.gender=="male"){
                        binding.cbMale.isChecked = true
                    }else{
                        binding.cbFemale.isChecked = true
                    }
                }
            }.addOnFailureListener { showToast("failed") }
    }

    fun checkBoxLogic(){
        binding.cbFemale.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                binding.cbMale.isChecked = false
            }
        }

        binding.cbMale.setOnCheckedChangeListener { compoundButton, b ->
            if (compoundButton.isChecked) {
                binding.cbFemale.isChecked = false
            }
        }
    }

    fun setDate(date: String){
        binding.etNextDate.setText(date)
    }

    fun saveDetails(){
        val doctorName = binding.tvDoctorName.text.toString()
        val dov = binding.tvDateOfVisit.text.toString()
        val patientName = binding.etName.text.toString()
        val age = parseInt()

        var gender = ""
        if(binding.cbMale.isChecked){
            gender = "male"
        }

        if(binding.cbFemale.isChecked){
            gender = "female"
        }
        val diagnosis = binding.etDiagnosis.text.toString()
        val presMeds = binding.etPrescribedMeds.text.toString()
        val ndov = binding.etNextDate.text.toString()

        val block = RecordBlock(doctorName, dov, patientName, age, gender, diagnosis, presMeds, ndov)
        FirebaseFirestore.getInstance().collection(Constants.DETAILS).document(getCurrentUserID()).collection(patientName).document(dov)
            .set(block, SetOptions.merge())
            .addOnSuccessListener { showToast("success") }.addOnFailureListener { showToast("failed") }

        /*val listdate = hashMapOf(
            dov to block
        )
        val array: ArrayList<HashMap<String, RecordBlock>> = ArrayList()
        array.add(listdate)
        val listname = hashMapOf(
            patientName to array
        )

        FirebaseFirestore.getInstance().collection(Constants.DETAILS).document(getCurrentUserID())
            .get().addOnSuccessListener {
                if(it.exists()){
                    if(it.get(patientName) != null) {
                        FirebaseFirestore.getInstance().collection(Constants.DETAILS)
                            .document(getCurrentUserID())
                            .update(
                                patientName,
                                FieldValue.arrayUnion(listdate)
                            ).addOnSuccessListener { showToast("success") }
                            .addOnFailureListener { showToast("failed") }
                    }else{
                        FirebaseFirestore.getInstance().collection(Constants.DETAILS).document(getCurrentUserID()).collection(patientName).document(dov)
                            .set(block, SetOptions.merge())
                            .addOnSuccessListener { showToast("success") }.addOnFailureListener { showToast("failed") }
                    }
                }else{
                    FirebaseFirestore.getInstance().collection(Constants.DETAILS).document(getCurrentUserID())
                        .set(listname, SetOptions.merge())
                        .addOnSuccessListener { showToast("success") }.addOnFailureListener { showToast("failed") }
                }
            }*/
    }

    fun parseInt(): Int{
        var age = 0
        try {
            age = Integer.parseInt(binding.etAge.text.toString())
        } catch (e: NumberFormatException) {
            // handle the exception
            age = 0
        }
        return age
    }
}