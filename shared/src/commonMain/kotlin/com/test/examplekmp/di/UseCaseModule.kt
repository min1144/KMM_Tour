package com.test.examplekmp.di

import com.test.examplekmp.domain.usecase.DetailUseCase
import com.test.examplekmp.domain.usecase.HomeUseCase
import com.test.examplekmp.domain.usecase.MapUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single { HomeUseCase(get()) }

    single { MapUseCase(get()) }

    single { DetailUseCase(get()) }
}