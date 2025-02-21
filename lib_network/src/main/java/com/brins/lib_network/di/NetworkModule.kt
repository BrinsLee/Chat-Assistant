package com.brins.lib_network.di

import com.brins.lib_base.model.adapter.GPTContentJsonAdapterFactory
import com.brins.lib_network.interceptor.HttpLoggerInterceptor
import com.brins.lib_network.interceptor.MockInterceptor
import com.brins.lib_network.interceptor.RequestHeaderInterceptor
import com.brins.lib_network.service.IChatGPTService
import com.brins.lib_network.service.IDeepSeekService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import retrofit2.create
import javax.inject.Qualifier


@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {

    const val OPEN_AI_BASE_URL = "https://api.openai.com/"

    const val DEEP_SEEK_BASE_URL = "https://api.deepseek.com/"

    // 自定义 Qualifier 注解（用于区分不同的 Retrofit 实例）
    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class OpenAIRetrofit

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class DeepSeekRetrofit

    /**
     * 提供公共的 OkHttpClient 实例，用于配置公共的请求头和拦截器等
     */
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(RequestHeaderInterceptor())
            .addInterceptor(HttpLoggerInterceptor())
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }


    @OpenAIRetrofit
    @Provides
    @Singleton
    fun provideOpenAIRetrofit(okHttpClient: OkHttpClient, moshiConverterFactory: MoshiConverterFactory): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(OPEN_AI_BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    @DeepSeekRetrofit
    @Provides
    @Singleton
    fun provideDeepSeekRetrofit(
        okHttpClient: OkHttpClient,
        moshiConverterFactory: MoshiConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(DEEP_SEEK_BASE_URL)
            .addConverterFactory(moshiConverterFactory)
            .build()
    }

    // 提供 OpenAI 的 Service 接口实例
    @Provides
    @Singleton
    fun provideOpenAIService(@OpenAIRetrofit retrofit: Retrofit): IChatGPTService {
        return retrofit.create(IChatGPTService::class.java)
    }

    // 提供 DeepSeek 的 Service 接口实例
    @Provides
    @Singleton
    fun provideDeepSeekService(@DeepSeekRetrofit retrofit: Retrofit): IDeepSeekService {
        return retrofit.create(IDeepSeekService::class.java)
    }

    /**
     * 提供 Moshi 实例，用于解析 JSON 数据
     */
    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder().add(GPTContentJsonAdapterFactory()).build()
    }


    /**
     * 提供 MoshiConverterFactory 实例，用于将 Moshi 实例与 Retrofit 集成
     */
    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

}