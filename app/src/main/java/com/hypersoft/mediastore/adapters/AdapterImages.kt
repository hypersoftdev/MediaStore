package com.hypersoft.mediastore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hypersoft.mediastore.R
import com.hypersoft.mediastore.databinding.ItemImagesBinding
import com.hypersoft.mediastore.datamodel.ImageItem
import com.hypersoft.mediastore.interfaces.OnImageClickListener

class AdapterImages(private val listener: OnImageClickListener) :
    ListAdapter<ImageItem, RecyclerView.ViewHolder>(diffUtilImages) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemImagesBinding>(
            layoutInflater,
            R.layout.item_images,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val currentItem = getItem(pos)
        val binding = (holder as ViewHolder<*>).globalBinding
        if (binding is ItemImagesBinding) {
            binding.apply {
                item = currentItem
                itemClick = listener
            }
        }
    }

    inner class ViewHolder<T : ViewBinding>(val globalBinding: T) :
        RecyclerView.ViewHolder(globalBinding.root)

    companion object {
        val diffUtilImages = object : DiffUtil.ItemCallback<ImageItem>() {
            override fun areItemsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ImageItem, newItem: ImageItem): Boolean {
                return oldItem.imagePath == newItem.imagePath
            }
        }
    }
}