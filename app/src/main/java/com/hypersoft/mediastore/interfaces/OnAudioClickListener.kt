package com.hypersoft.mediastore.interfaces

import com.hypersoft.mediastore.datamodel.AudioItem

interface OnAudioClickListener {
    fun onAudioClick(audioItem: AudioItem)
}