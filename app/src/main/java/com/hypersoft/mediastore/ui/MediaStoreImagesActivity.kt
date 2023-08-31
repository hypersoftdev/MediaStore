package com.hypersoft.mediastore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.R
import com.hypersoft.mediastore.databinding.ActivityMediaStoreAudioBinding
import com.hypersoft.mediastore.databinding.ActivityMediaStoreImagesBinding

class MediaStoreImagesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaStoreImagesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaStoreImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}