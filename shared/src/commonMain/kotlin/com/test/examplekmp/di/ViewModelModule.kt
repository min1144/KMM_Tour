package com.test.examplekmp.di

import com.test.examplekmp.presentation.ui.detail.DetailViewModel
import com.test.examplekmp.presentation.ui.home.HomeViewModel
import com.test.examplekmp.presentation.ui.map.MapViewModel
import com.test.examplekmp.presentation.ui.splash.SplashViewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val viewModelModule: Module = module {
    factory { SplashViewModel() }
    factory { HomeViewModel( get() ) }
    factory { MapViewModel( get() ) }
    factory { (contentId: String, contentTypeId: String) -> DetailViewModel(get(), contentId, contentTypeId) }
}