package com.miniproj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miniproj.R
import com.miniproj.databinding.ActivityRecordBlocksBinding

class RecordBlockActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRecordBlocksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBlocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patientName = intent.getStringExtra("patient")
        binding.textView2.text = patientName

    }
}