package com.test.examplekmp.di

import com.test.examplekmp.domain.util.delegate.AppConfigDelegate
import com.test.examplekmp.util.delegate.AppConfigDelegateImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val delegateModule = module {
    singleOf<AppConfigDelegate>(::AppConfigDelegateImpl)
}