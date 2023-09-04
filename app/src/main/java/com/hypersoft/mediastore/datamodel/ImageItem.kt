package com.hypersoft.mediastore.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageItem(
    var id: Long,
    var imageName:String,
    var imageSizeTitle:String,
    var imageSize:Long,
    var imageDateAddedTitle:String,
    var imageDateAdded:Long,
    var imageDateModifiedTitle:String,
    var imageDateModified:Long,
    var imageMimeType:String,
    var imagePath: String
): Parcelable