package com.hypersoft.mediastore.observers

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
class MediaContentObserver(private val contentResolver: ContentResolver,private val mediaType:Uri, private val onChangeCallback: () -> Unit) : ContentObserver(
    Handler(Looper.getMainLooper())
) {

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        super.onChange(selfChange, uri)

        // Call the provided callback when a change is detected
        onChangeCallback.invoke()
    }

    fun register() {
        contentResolver.registerContentObserver(mediaType, true, this)
    }

    fun unregister() {
        contentResolver.unregisterContentObserver(this)
    }
}