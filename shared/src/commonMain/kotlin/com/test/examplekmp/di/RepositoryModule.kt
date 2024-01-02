package com.test.examplekmp.di

import com.test.examplekmp.data.repository.DetailRepositoryImpl
import com.test.examplekmp.data.repository.HomeListRepositoryImpl
import com.test.examplekmp.data.repository.MapListRepositoryImpl
import com.test.examplekmp.domain.repository.DetailRepository
import com.test.examplekmp.domain.repository.HomeListRepository
import com.test.examplekmp.domain.repository.MapListRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory<HomeListRepository> { HomeListRepositoryImpl(get()) }

    factory<MapListRepository> { MapListRepositoryImpl(get()) }

    factory<DetailRepository> { DetailRepositoryImpl(get()) }

}