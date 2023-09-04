package com.hypersoft.mediastore.interfaces

import com.hypersoft.mediastore.datamodel.VideoItem

interface OnVideoClickListener {
    fun onVideoClick(videoItem: VideoItem)
}