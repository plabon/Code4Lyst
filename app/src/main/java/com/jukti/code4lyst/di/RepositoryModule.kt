package com.jukti.code4lyst.di

import com.jukti.code4lyst.data.remote.DogDataSource
import com.jukti.code4lyst.data.repository.DogRepositoryImp
import com.jukti.code4lyst.domain.DogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideClearScoreRepository(dataSource: DogDataSource) : DogRepository {
        return DogRepositoryImp(dataSource)
    }
}