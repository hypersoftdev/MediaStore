package com.hypersoft.mediastore.interfaces

import com.hypersoft.mediastore.datamodel.AudioItem

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
interface OnAudioClickListener {
    fun onAudioClick(audioItem: AudioItem)
}