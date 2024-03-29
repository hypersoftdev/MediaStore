package com.hypersoft.mediastore.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.hypersoft.mediastore.R
import com.hypersoft.mediastore.databinding.ItemAudiosBinding
import com.hypersoft.mediastore.datamodel.AudioItem
import com.hypersoft.mediastore.interfaces.OnAudioClickListener

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class AdapterAudios(private val listener: OnAudioClickListener) :
    ListAdapter<AudioItem, RecyclerView.ViewHolder>(diffUtilAudio) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = DataBindingUtil.inflate<ItemAudiosBinding>(
            layoutInflater,
            R.layout.item_audios,
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, pos: Int) {
        val currentItem = getItem(pos)
        val binding = (holder as ViewHolder<*>).globalBinding
        if (binding is ItemAudiosBinding) {
            binding.apply {
                item = currentItem
                itemClick = listener
            }
        }
    }

    inner class ViewHolder<T : ViewBinding>(val globalBinding: T) :
        RecyclerView.ViewHolder(globalBinding.root)

    companion object {
        val diffUtilAudio = object : DiffUtil.ItemCallback<AudioItem>() {
            override fun areItemsTheSame(oldItem: AudioItem, newItem: AudioItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: AudioItem, newItem: AudioItem): Boolean {
                return oldItem.audioPath == newItem.audioPath
            }
        }
    }
}