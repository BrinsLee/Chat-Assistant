
package com.brins.lib_base.di

import android.content.Context
//import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.getstream.chat.android.client.ChatClient
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ChatModule {

  @Provides
  @Singleton
  fun provideStreamChatClient() = ChatClient.instance()

/*  @Provides
  @Singleton
  fun provideChatWorker(@ApplicationContext context: Context): WorkManager {
    return WorkManager.getInstance(context)
  }*/
}
