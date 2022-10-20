package com.miniproj.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.miniproj.databinding.ActivityDetailsBinding

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("date")
        binding.tv.text = text
    }
}