package com.hypersoft.mediastore.di

import com.hypersoft.mediastore.viewmodel.AudiosViewModel
import com.hypersoft.mediastore.viewmodel.ImagesViewModel
import com.hypersoft.mediastore.viewmodel.VideosViewModel
import org.koin.dsl.module

/**
 * @Author: Muhammad Yaqoob
 * @Date: 29,March,2024.
 * @Accounts
 *      -> https://github.com/orbitalsonic
 *      -> https://www.linkedin.com/in/myaqoob7
 */
private val viewModelsModules = module {
    single { ImagesViewModel() }
    single { VideosViewModel() }
    single { AudiosViewModel() }
}

val modulesList = listOf(viewModelsModules)