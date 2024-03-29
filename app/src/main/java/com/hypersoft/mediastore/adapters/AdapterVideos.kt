package com.hypersoft.mediastore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hypersoft.mediastore.R
import com.hypersoft.mediastore.databinding.ItemVideosBinding
import com.hypersoft.mediastore.datamodel.VideoItem
import com.hypersoft.mediastore.interfaces.OnVideoClickListener

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class AdapterVideos(private val listener: OnVideoClickListener) :
    ListAdapter<VideoItem, RecyclerView.ViewHolder>(diffUtilVideos) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemVideosBinding>(
            layoutInflater,
            R.layout.item_videos,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val currentItem = getItem(pos)
        val binding = (holder as ViewHolder<*>).globalBinding
        if (binding is ItemVideosBinding) {
            binding.apply {
                item = currentItem
                itemClick = listener
            }
        }
    }

    inner class ViewHolder<T : ViewBinding>(val globalBinding: T) :
        RecyclerView.ViewHolder(globalBinding.root)

    companion object {
        val diffUtilVideos = object : DiffUtil.ItemCallback<VideoItem>() {
            override fun areItemsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: VideoItem, newItem: VideoItem): Boolean {
                return oldItem.videoPath == newItem.videoPath
            }
        }
    }
}