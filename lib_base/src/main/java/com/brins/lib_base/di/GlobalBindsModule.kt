package com.brins.lib_base.di

import com.brins.lib_base.utils.CustomChatClientDebugger
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.debugger.ChatClientDebugger

@Module
@InstallIn(SingletonComponent::class)
interface GlobalBindsModule {


    @Binds
    fun bindCustomChatClientDebugger(customChatClientDebugger: CustomChatClientDebugger): ChatClientDebugger
}