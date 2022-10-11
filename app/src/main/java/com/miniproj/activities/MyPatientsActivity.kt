package com.miniproj.activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.miniproj.R
import com.miniproj.adapter.ItemAdapter
import com.miniproj.databinding.ActivityMyPatientsBinding
import com.miniproj.utils.Constants
import kotlin.collections.ArrayList

class MyPatientsActivity : BaseActivity() {
    private lateinit var binding: ActivityMyPatientsBinding
    private lateinit var patientListFirestore: ArrayList<String>
    private lateinit var patientAdapter: ItemAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPatientsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        patientListFirestore = ArrayList()
        fSetupRV()

        binding.btnAddPatient.setOnClickListener {
            val addPatientDialog = Dialog(this, R.style.ThemeOverlay_AppCompat_Dialog)
            addPatientDialog.setContentView(R.layout.dialog_add_patient)
            addPatientDialog.findViewById<AppCompatButton>(R.id.btn_dialog_add).setOnClickListener {
                val name = addPatientDialog.findViewById<EditText>(R.id.et_dialog_patient_name).text.toString()
                FirebaseFirestore.getInstance().collection(Constants.PATIENT_LIST).document(getCurrentUserID())
                    .get().addOnSuccessListener {
                        if (it.exists()){
                            FirebaseFirestore.getInstance().collection(Constants.PATIENT_LIST).document(getCurrentUserID())
                                .update(Constants.PATIENT_LIST, FieldValue.arrayUnion(name)).addOnSuccessListener {
                                    fSetupRV()
                                    showToast("patient added successfully")
                                }.addOnFailureListener {
                                    showToast("failed")
                                }
                        }else{
                            val array: ArrayList<String> = ArrayList()
                            array.add(name)
                            val list = hashMapOf(
                                Constants.PATIENT_LIST to array
                            )
                            FirebaseFirestore.getInstance().collection(Constants.PATIENT_LIST).document(getCurrentUserID())
                                .set(list, SetOptions.merge()).addOnSuccessListener {
                                    fSetupRV()
                                    showToast("patient added successfully")
                                }.addOnFailureListener {
                                    showToast("failed")
                                }
                        }
                    }.addOnFailureListener {
                        showToast("patient addition failed")
                    }
                addPatientDialog.dismiss()
            }
            addPatientDialog.show()
        }
    }

    private fun fSetupRV(){
        FirebaseFirestore.getInstance().collection(Constants.PATIENT_LIST).document(getCurrentUserID())
            .get().addOnSuccessListener {
                patientListFirestore = it.get(Constants.PATIENT_LIST) as ArrayList<String>
                setupRV(patientListFirestore)
            }
    }

    private fun setupRV(list: ArrayList<String>) {

        binding.rvPatientList.setHasFixedSize(true)
        binding.rvPatientList.layoutManager = LinearLayoutManager(this)
        list.reverse()
        patientAdapter = ItemAdapter(list)
        binding.rvPatientList.adapter = patientAdapter
        patientAdapter.onItemClick = {
            val intent = Intent(this, RecordBlockActivity::class.java)
            intent.putExtra("patient", it)
            startActivity(intent)
        }
    }
}