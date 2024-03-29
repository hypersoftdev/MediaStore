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
data class VideoItem(
    var id: Long,
    var videoName:String,
    var videoSizeTitle:String,
    var videoSize:Long,
    var videoDurationTitle:String,
    var videoDuration:Long,
    var videoDateAddedTitle:String,
    var videoDateAdded:Long,
    var videoDateModifiedTitle:String,
    var videoDateModified:Long,
    var videoHeightTitle:String,
    var videoHeight:Long,
    var videoResolution:String,
    var videoMimeType:String,
    var videoPath: String
): Parcelable