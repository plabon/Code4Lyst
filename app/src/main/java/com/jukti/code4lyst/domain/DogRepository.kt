package com.jukti.code4lyst.domain

import com.jukti.code4lyst.data.model.Breed
import com.jukti.code4lyst.data.model.Image
import com.jukti.code4lyst.data.model.ResponseWrapper
import kotlinx.coroutines.flow.Flow

interface DogRepository {
    suspend fun getAllDogBreeds(): Flow<ResponseWrapper<List<Breed>>>
    suspend fun getImageByBreedId(breedId:Int,limit:Int): Flow<ResponseWrapper<List<Image>>>
    }