package com.hypersoft.mediastore.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.databinding.ActivityMainBinding
import com.hypersoft.mediastore.utils.PermissionUtils.AUDIO_PERMISSIONS
import com.hypersoft.mediastore.utils.PermissionUtils.IMAGES_PERMISSIONS
import com.hypersoft.mediastore.utils.PermissionUtils.VIDEOS_PERMISSIONS
import com.hypersoft.mediastore.utils.PermissionUtils.isAudioPermissionGranted
import com.hypersoft.mediastore.utils.PermissionUtils.isImagesPermissionGranted
import com.hypersoft.mediastore.utils.PermissionUtils.isVideosPermissionGranted

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
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
            if (isImagesPermissionGranted()) {
                startActivity(Intent(this, MediaStoreImagesActivity::class.java))
            } else {
                askForImagesPermission()
            }
        }

        binding.btnVideos.setOnClickListener {
            if (isVideosPermissionGranted()) {
                startActivity(Intent(this, MediaStoreVideosActivity::class.java))
            } else {
                askForVideosPermission()
            }
        }

        binding.btnAudios.setOnClickListener {
            if (isAudioPermissionGranted()) {
                startActivity(Intent(this, MediaStoreAudioActivity::class.java))
            } else {
                askForAudioPermission()
            }

        }
    }

    /**
     * For Images Permissions
     */
    private fun askForImagesPermission() {
        requestImagesPermissionLauncher.launch(IMAGES_PERMISSIONS)
    }

    private val requestImagesPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = true
            IMAGES_PERMISSIONS.forEach {
                if (permissions[it] != true) {
                    isGranted = false
                    return@forEach
                }
            }
            if (isGranted) {
                onImagesPermissionGranted()
            }else{
                onImagesPermissionNotGranted()
            }
        }

    private fun onImagesPermissionGranted() {
        startActivity(Intent(this, MediaStoreImagesActivity::class.java))
    }

    private fun onImagesPermissionNotGranted() {
        showMessage("Please grant permission to access all Images")
    }

    /**
     * For Videos Permissions
     */
    private fun askForVideosPermission() {
        requestVideosPermissionLauncher.launch(VIDEOS_PERMISSIONS)
    }

    private val requestVideosPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = true
            VIDEOS_PERMISSIONS.forEach {
                if (permissions[it] != true) {
                    isGranted = false
                    return@forEach
                }
            }
            if (isGranted) {
                onVideosPermissionGranted()
            }else{
                onVideosPermissionNotGranted()
            }
        }

    private fun onVideosPermissionGranted() {
        startActivity(Intent(this, MediaStoreVideosActivity::class.java))
    }

    private fun onVideosPermissionNotGranted() {
        showMessage("Please grant permission to access all Videos")
    }


    /**
     * For Audios Permissions
     */
    private fun askForAudioPermission() {
        requestAudioPermissionLauncher.launch(AUDIO_PERMISSIONS)
    }

    private val requestAudioPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var isGranted = true
            AUDIO_PERMISSIONS.forEach {
                if (permissions[it] != true) {
                    isGranted = false
                    return@forEach
                }
            }
            if (isGranted) {
                onAudioPermissionGranted()
            }else{
                onAudioPermissionNotGranted()
            }
        }

    private fun onAudioPermissionGranted() {
        startActivity(Intent(this, MediaStoreAudioActivity::class.java))
    }

    private fun onAudioPermissionNotGranted() {
        showMessage("Please grant permission to access all Audios")
    }



    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}