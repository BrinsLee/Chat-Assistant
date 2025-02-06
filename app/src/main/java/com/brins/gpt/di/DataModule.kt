package com.brins.gpt.di

import com.brins.gpt.repository.GPTAudioRepositoryImpl
import com.brins.gpt.repository.GPTChannelRepositoryImpl
import com.brins.gpt.repository.GPTImageRepositoryImpl
import com.brins.gpt.repository.GPTUserInfoRepositoryImpl
import com.brins.gpt.repository.GPTMessageRepositoryImpl
import com.brins.gpt.repository.IGPTAudioRepository
import com.brins.gpt.repository.IGPTChannelRepository
import com.brins.gpt.repository.IGPTImageRepository
import com.brins.gpt.repository.IGPTUserInfoRepository
import com.brins.gpt.repository.IGPTMessageRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindGPTChatMessageRepository(gptChatMessageRepository: GPTMessageRepositoryImpl): IGPTMessageRepository


    @Binds
    fun bindGPTChatAudioRepository(gptChatAudioRepository: GPTAudioRepositoryImpl): IGPTAudioRepository


    @Binds
    fun bindGPTUserInfoRepository(gptUserInfoRepositoryImpl: GPTUserInfoRepositoryImpl): IGPTUserInfoRepository


    @Binds
    fun bindGPTChannelRepository(gptChannelRepositoryImpl: GPTChannelRepositoryImpl): IGPTChannelRepository

    @Binds
    fun bindGPTImageRepository(gptImageRepositoryImpl: GPTImageRepositoryImpl): IGPTImageRepository
}