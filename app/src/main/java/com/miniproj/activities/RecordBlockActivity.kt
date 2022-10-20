package com.miniproj.activities

import android.icu.text.AlphabeticIndex
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.miniproj.adapter.RecordAdapter
import com.miniproj.databinding.ActivityRecordBlocksBinding
import com.miniproj.model.RecordBlock

class RecordBlockActivity : AppCompatActivity() {
    private lateinit var recordsAdapter: RecordAdapter
    private lateinit var list: ArrayList<RecordBlock>
    private lateinit var binding: ActivityRecordBlocksBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecordBlocksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val patientName = intent.getStringExtra("patient")
        list = ArrayList()

        binding.btnAddRecord.setOnClickListener {
            list.add(RecordBlock("","",0,"","","",""))
            binding.tvNoBlocks.visibility = View.GONE
            binding.rvRecordBlocks.visibility = View.VISIBLE

            binding.rvRecordBlocks.setHasFixedSize(true)
            binding.rvRecordBlocks.layoutManager = LinearLayoutManager(this)
            //recordsAdapter = RecordAdapter(list)
            binding.rvRecordBlocks.adapter = recordsAdapter
        }

    }
}