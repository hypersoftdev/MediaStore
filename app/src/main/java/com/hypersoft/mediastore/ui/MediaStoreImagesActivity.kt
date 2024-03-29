package com.hypersoft.mediastore.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hypersoft.mediastore.adapters.AdapterImages
import com.hypersoft.mediastore.databinding.ActivityMediaStoreImagesBinding
import com.hypersoft.mediastore.datamodel.ImageItem
import com.hypersoft.mediastore.di.DIComponent
import com.hypersoft.mediastore.interfaces.OnImageClickListener
import com.hypersoft.mediastore.observers.MediaContentObserver

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class MediaStoreImagesActivity : AppCompatActivity(), OnImageClickListener {

    private lateinit var binding: ActivityMediaStoreImagesBinding

    private val diComponent by lazy { DIComponent() }
    private val mAdapter by lazy { AdapterImages(this) }

    private lateinit var mediaContentObserver: MediaContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaStoreImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initViewModel()
        initMediaObserver()
    }

    private fun initMediaObserver() {
        mediaContentObserver = MediaContentObserver(contentResolver, MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
            diComponent.imagesViewModel.mediaObserve(this)
            Toast.makeText(this, "Data has been updated", Toast.LENGTH_SHORT).show()
        }
        mediaContentObserver.register()
    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() {
        diComponent.imagesViewModel.apply {
            fetchingImages(this@MediaStoreImagesActivity)
            imageUpdate.observe(this@MediaStoreImagesActivity) {
                try {
                    if (it) {
                        viewsSettings(isImagesListEmpty())
                        mAdapter.submitList(getAllImages())
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
            binding.pbLoadingImage.visibility = View.GONE
            if (visibility) {
                binding.tvNoImageFound.visibility = View.VISIBLE
            } else {
                binding.tvNoImageFound.visibility = View.GONE
            }
        } catch (ex: Exception) {
            Log.e("initViewModelTAG", "${ex.message}")
        }
    }

    private fun initRecyclerView() {
        binding.recyclerViewImage.adapter = mAdapter
    }

    override fun onImageClick(imageItem: ImageItem) {
        showMessage(imageItem.imageName)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaContentObserver.unregister()
    }
}