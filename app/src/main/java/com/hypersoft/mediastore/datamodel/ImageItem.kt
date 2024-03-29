package com.hypersoft.mediastore.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
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