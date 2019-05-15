package com.basecode

import android.content.Context
import org.koin.dsl.module.Module
import org.koin.dsl.module.module

object AppKoinModules {
    fun modules(): List<Module> {
        return listOf(mainModule)
    }

    private val mainModule = module {
        single {
            App.appContext as Context
        }
    }
}