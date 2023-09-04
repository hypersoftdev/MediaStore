package com.hypersoft.mediastore.interfaces

import com.hypersoft.mediastore.datamodel.ImageItem

interface OnImageClickListener {
    fun onImageClick(imageItem: ImageItem)
}