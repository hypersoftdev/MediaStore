package com.hypersoft.mediastore.di

import com.hypersoft.mediastore.viewmodel.AudiosViewModel
import com.hypersoft.mediastore.viewmodel.ImagesViewModel
import com.hypersoft.mediastore.viewmodel.VideosViewModel
import org.koin.dsl.module


private val viewModelsModules = module {
    single { ImagesViewModel() }
    single { VideosViewModel() }
    single { AudiosViewModel() }
}

val modulesList = listOf(viewModelsModules)