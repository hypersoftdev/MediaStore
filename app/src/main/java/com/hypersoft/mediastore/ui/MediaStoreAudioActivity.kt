package com.hypersoft.mediastore.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.adapters.AdapterAudios
import com.hypersoft.mediastore.databinding.ActivityMediaStoreAudioBinding
import com.hypersoft.mediastore.datamodel.AudioItem
import com.hypersoft.mediastore.di.DIComponent
import com.hypersoft.mediastore.interfaces.OnAudioClickListener
import com.hypersoft.mediastore.observers.MediaContentObserver

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class MediaStoreAudioActivity : AppCompatActivity(), OnAudioClickListener {

    private lateinit var binding: ActivityMediaStoreAudioBinding

    private val diComponent by lazy { DIComponent() }
    private val mAdapter by lazy { AdapterAudios(this) }

    private lateinit var mediaContentObserver: MediaContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaStoreAudioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initViewModel()
        initMediaObserver()
    }

    private fun initMediaObserver() {
        mediaContentObserver = MediaContentObserver(contentResolver, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI) {
            diComponent.audiosViewModel.mediaObserve(this)
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show()
        }
        mediaContentObserver.register()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        diComponent.audiosViewModel.apply {
            fetchingAudios(this@MediaStoreAudioActivity)
            audioUpdate.observe(this@MediaStoreAudioActivity) {
                try {
                    if (it) {
                        viewsSettings(isAudiosListEmpty())
                        mAdapter.submitList(getAllAudios())
                    } else {
                        viewsSettings(false)
                    }
                } catch (ex: Exception) {
                    Log.e("initViewModelTAG", "${ex.message}")
                }
            }
        }
    }

    private fun viewsSettings(visibility: Boolean) {
        try {
            binding.pbLoadingAudios.visibility = View.GONE
            if (visibility) {
                binding.tvNoAudiosFound.visibility = View.VISIBLE
            } else {
                binding.tvNoAudiosFound.visibility = View.GONE
            }
        } catch (ex: Exception) {
            Log.e("initViewModelTAG", "${ex.message}")
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewAudios.adapter = mAdapter
    }

    override fun onAudioClick(audioItem: AudioItem) {
        showMessage(audioItem.audioName)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaContentObserver.unregister()
    }
}