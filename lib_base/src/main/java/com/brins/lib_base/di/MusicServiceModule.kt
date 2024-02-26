package com.brins.lib_base.di

import com.brins.lib_base.controller.MusicPlayerController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicServiceModule {


    @Provides
    @Singleton
    fun provideMusicPlayerController(): MusicPlayerController {
        return MusicPlayerController()
    }
}