package com.hypersoft.mediastore.adapters

import android.net.Uri
import android.widget.ImageView
import androidx.constraintlayout.utils.widget.ImageFilterView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.hypersoft.mediastore.R
import java.io.File

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */

/**
 *  Types of ImageView
 *      -> ImageView
 *      -> ImageFilterView
 *      -> ShapeableImageView
 *
 *  Customize following methods according to your ImageView class.
 */

/* ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ */

/**
 * @param: imageId -> Set image resource_id for this (e.g. R.drawable.img_dummy)
 *  Syntax:
 *      xml     -> app:imageId="@{imageResource}"
 *      kotlin  -> binding.imageView.setImageFromResource(R.drawable.img_dummy)
 */

@BindingAdapter("imageId")
fun ImageView.setImageFromResource(imageId: Int) {
    Glide
        .with(this)
        .load(imageId)
        .into(this)
}
@BindingAdapter("vdPath")
fun ImageFilterView.setVideoFromPath(vdPath:String) {
    Glide
        .with(this)
        .load(vdPath)
        .placeholder(R.drawable.ic_video_placeholder)
        .into(this)
}

@BindingAdapter("imagePath")
fun ImageFilterView.setImageFromPath(imagePath:String) {
    Glide
        .with(this)
        .load(imagePath)
        .placeholder(R.drawable.ic_image_placeholder)
        .into(this)
}

/**
 * @param: imageUri -> Set image uri for this
 *  Syntax:
 *      xml     -> app:imageUri="@{uri}"
 *      kotlin  -> binding.imageView.setImageFromUri(uri)
 */

@BindingAdapter("imageUri")
fun ImageView.setImageFromUri(imageUri: Uri) {
    Glide
        .with(this)
        .load(imageUri)
        .into(this)
}

/**
 * @param: imageFilePath -> Set image file path for this (e.g. File)
 *  Syntax:
 *      xml     -> app:imageFilePath="@{file}"
 *      kotlin  -> binding.imageView.setImageFromFilePath(file)
 */

@BindingAdapter("imageFilePath")
fun ImageView.setImageFromFilePath(imageFilePath: File) {
    Glide
        .with(this)
        .load(imageFilePath.toString())
        .into(this)
}
