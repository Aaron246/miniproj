package com.miniproj.activities

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.miniproj.R
import com.miniproj.adapter.RecordAdapter
import com.miniproj.databinding.ActivityRecordBinding
import com.miniproj.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RecordActivity : BaseActivity() {
    private lateinit var binding: ActivityRecordBinding
    private lateinit var recordAdapter: RecordAdapter
    private lateinit var list: ArrayList<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRecordBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        list = ArrayList()
        FirebaseFirestore.getInstance().collection(Constants.DATE_LIST).document(getCurrentUserID())
            .get().addOnSuccessListener {
                if (it.exists()){
                    binding.tvNoRecord.visibility = View.GONE
                    binding.rvRecordsList.visibility = View.VISIBLE
                    fSetupRV()
                }
            }

        binding.fabCreateRecord.setOnClickListener {
            binding.tvNoRecord.visibility = View.GONE
            binding.rvRecordsList.visibility = View.VISIBLE

            var cal = Calendar.getInstance()

            val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val myFormat = "dd MMM yyyy" // mention the format you need
                val sdf = SimpleDateFormat(myFormat, Locale.US)
                FirebaseFirestore.getInstance().collection(Constants.DATE_LIST).document(getCurrentUserID())
                    .get().addOnSuccessListener {
                        if (it.exists()){
                            FirebaseFirestore.getInstance().collection(Constants.DATE_LIST).document(getCurrentUserID())
                                .update(Constants.DATE_LIST, FieldValue.arrayUnion(sdf.format(cal.time))).addOnSuccessListener {
                                    fSetupRV()
                                    showToast("date added successfully")
                                }.addOnFailureListener {
                                    showToast("failed")
                                }
                        }else{
                            val array: ArrayList<String> = ArrayList()
                            array.add(sdf.format(cal.time))
                            val list = hashMapOf(
                                Constants.DATE_LIST to array
                            )
                            FirebaseFirestore.getInstance().collection(Constants.DATE_LIST).document(getCurrentUserID())
                                .set(list, SetOptions.merge()).addOnSuccessListener {
                                    binding.tvNoRecord.visibility = View.GONE
                                    binding.rvRecordsList.visibility = View.VISIBLE
                                    fSetupRV()
                                    showToast("date added successfully")
                                }.addOnFailureListener {
                                    showToast("failed")
                                }
                        }
                    }.addOnFailureListener {
                        showToast("date addition failed")
                    }
            }
            DatePickerDialog(this, dateSetListener,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show()
        }
    }

    private fun fSetupRV(){
        FirebaseFirestore.getInstance().collection(Constants.DATE_LIST).document(getCurrentUserID())
            .get().addOnSuccessListener {
                list = it.get(Constants.DATE_LIST) as ArrayList<String>
                setupRV(list)
            }
    }

    private fun setupRV(list: ArrayList<String>){
        binding.rvRecordsList.setHasFixedSize(true)
        binding.rvRecordsList.layoutManager = LinearLayoutManager(this)
        list.reverse()
        recordAdapter = RecordAdapter(list)
        binding.rvRecordsList.adapter = recordAdapter
        recordAdapter.onItemClick = {
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra("date", it)
            startActivity(intent)
        }
    }
}