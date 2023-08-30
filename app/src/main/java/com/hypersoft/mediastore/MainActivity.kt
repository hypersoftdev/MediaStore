package com.hypersoft.mediastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {
        binding.btnImages.setOnClickListener {

        }

        binding.btnVideos.setOnClickListener {

        }

        binding.btnAudios.setOnClickListener {

        }
    }
}