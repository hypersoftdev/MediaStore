package com.hypersoft.mediastore.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.R
import com.hypersoft.mediastore.databinding.ActivityMediaStoreAudioBinding
import com.hypersoft.mediastore.databinding.ActivityMediaStoreVideosBinding

class MediaStoreVideosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaStoreVideosBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaStoreVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}