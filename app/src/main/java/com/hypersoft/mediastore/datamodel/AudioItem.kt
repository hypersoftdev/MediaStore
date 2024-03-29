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
data class AudioItem(
    var id: Long,
    var trackNumber: Int,
    var audioName: String,
    var audioSizeTitle: String,
    var audioSize: Long,
    var audioDurationTitle: String,
    var audioDuration: Long,
    var audioDateAddedTitle: String,
    var audioDateAdded: Long,
    var audioDateModifiedTitle: String,
    var audioDateModified: Long,
    var albumId: Long,
    var albumName: String,
    var artistId: Long,
    var artistName: String,
    var composerName: String,
    var albumArtist: String,
    var audioPath: String
): Parcelable
