package com.jukti.code4lyst.data.repository

import android.util.Log
import com.jukti.code4lyst.data.model.Breed
import com.jukti.code4lyst.data.model.Image
import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.data.remote.DogDataSource
import com.jukti.code4lyst.domain.DogRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton



@Singleton
class DogRepositoryImp @Inject constructor(private val remoteDataSource: DogDataSource):DogRepository{

    override suspend fun getAllDogBreeds(): Flow<ResponseWrapper<List<Breed>>> {
        return flow {
            Log.e(TAG, "I'm working in thread ${Thread.currentThread().name}")
            emit(remoteDataSource.getAllBreeds())
        }
    }

    override suspend fun getImageByBreedId(
        breedId: Int,
        limit: Int
    ): Flow<ResponseWrapper<List<Image>>> {
        return flow {
            Log.e(TAG, "I'm working in thread ${Thread.currentThread().name}")
            emit(remoteDataSource.getImagesByBreed(breedId,limit))
        }
    }

    companion object{
        private const val TAG = "ClearScoreRepository"
    }



}