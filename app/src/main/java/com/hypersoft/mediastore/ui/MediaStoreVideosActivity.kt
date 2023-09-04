package com.hypersoft.mediastore.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.adapters.AdapterVideos
import com.hypersoft.mediastore.databinding.ActivityMediaStoreVideosBinding
import com.hypersoft.mediastore.datamodel.VideoItem
import com.hypersoft.mediastore.di.DIComponent
import com.hypersoft.mediastore.interfaces.OnVideoClickListener

class MediaStoreVideosActivity : AppCompatActivity(), OnVideoClickListener {

    private lateinit var binding: ActivityMediaStoreVideosBinding

    private val diComponent by lazy { DIComponent() }
    private val mAdapter by lazy { AdapterVideos(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaStoreVideosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initViewModel()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        diComponent.videosViewModel.apply {
            fetchingVideos(this@MediaStoreVideosActivity)
            videoUpdate.observe(this@MediaStoreVideosActivity) {
                try {
                    if (it) {
                        viewsSettings(isVideosListEmpty())
                        mAdapter.submitList(getAllVideos())
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
            binding.pbLoadingVideo.visibility = View.GONE
            if (visibility) {
                binding.tvNoVideoFound.visibility = View.VISIBLE
            } else {
                binding.tvNoVideoFound.visibility = View.GONE
            }
        } catch (ex: Exception) {
            Log.e("initViewModelTAG", "${ex.message}")
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewVideo.adapter = mAdapter
    }

    override fun onVideoClick(videoItem: VideoItem) {
        showMessage(videoItem.videoName)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}