package com.sm.borutoapp.di

import androidx.paging.ExperimentalPagingApi
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sm.borutoapp.data.local.BorutoDatabase
import com.sm.borutoapp.data.remote.BorutoApi
import com.sm.borutoapp.data.repository.RemoteDataSourceImpl
import com.sm.borutoapp.domain.repository.RemoteDataSource
import com.sm.borutoapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@ExperimentalPagingApi
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .readTimeout(15 , TimeUnit.SECONDS)
            .connectTimeout(15 , TimeUnit.SECONDS)
            .build()
    }

    @ExperimentalSerializationApi
    @Provides
    @Singleton
    fun provideRetrofitInstance(okHttpClient: OkHttpClient) : Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
    }

    @Provides
    @Singleton
    fun provideBorutoApi(retrofit:Retrofit) : BorutoApi {
         return retrofit.create(BorutoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideRemoteDataSource(borutoApi: BorutoApi , borutoDatabase: BorutoDatabase) : RemoteDataSource{
        return RemoteDataSourceImpl(
            borutoApi = borutoApi ,
            borutoDatabase = borutoDatabase
        )
    }

}