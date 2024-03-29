package com.hypersoft.mediastore.utils

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
object PermissionUtils {
    val IMAGES_PERMISSIONS = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    } else {
        mutableListOf(
            Manifest.permission.READ_MEDIA_IMAGES
        ).toTypedArray()
    }

    val VIDEOS_PERMISSIONS = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    } else {
        mutableListOf(
            Manifest.permission.READ_MEDIA_VIDEO
        ).toTypedArray()
    }

    val AUDIO_PERMISSIONS = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        mutableListOf(
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).apply {
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    } else {
        mutableListOf(
            Manifest.permission.READ_MEDIA_AUDIO
        ).toTypedArray()
    }

    fun Activity?.isImagesPermissionGranted(): Boolean {
        this?.let { mActivity ->
            IMAGES_PERMISSIONS.forEach {
                if (ContextCompat.checkSelfPermission(
                        mActivity,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }
        return false
    }

    fun Activity?.isVideosPermissionGranted(): Boolean {
        this?.let { mActivity ->
            VIDEOS_PERMISSIONS.forEach {
                if (ContextCompat.checkSelfPermission(
                        mActivity,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }
        return false
    }

    fun Activity?.isAudioPermissionGranted(): Boolean {
        this?.let { mActivity ->
            AUDIO_PERMISSIONS.forEach {
                if (ContextCompat.checkSelfPermission(
                        mActivity,
                        it
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return false
                }
            }
            return true
        }
        return false
    }
}