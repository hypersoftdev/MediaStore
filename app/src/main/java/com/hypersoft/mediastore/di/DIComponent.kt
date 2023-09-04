package com.hypersoft.mediastore.di

import com.hypersoft.mediastore.viewmodel.AudiosViewModel
import com.hypersoft.mediastore.viewmodel.ImagesViewModel
import com.hypersoft.mediastore.viewmodel.VideosViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DIComponent : KoinComponent {

    // ViewModels
    val imagesViewModel by inject<ImagesViewModel>()
    val videosViewModel by inject<VideosViewModel>()
    val audiosViewModel by inject<AudiosViewModel>()
}