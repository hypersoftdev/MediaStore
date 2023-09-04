package com.hypersoft.mediastore.datamodel

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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