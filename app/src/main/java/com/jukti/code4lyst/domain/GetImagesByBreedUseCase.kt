package com.jukti.code4lyst.domain

import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.data.model.toBreedDomain
import com.jukti.code4lyst.data.model.toImageDomainModel
import com.jukti.code4lyst.domain.model.BreedDomainModel
import com.jukti.code4lyst.domain.model.ImageDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetImagesByBreedUseCase @Inject constructor(private val repository : DogRepository) {
    suspend fun execute(breedId:Int,limit:Int):Flow<ResponseWrapper<List<ImageDomainModel>>>{
        return repository.getImageByBreedId(breedId,limit).map {
            when(it){
                is ResponseWrapper.Success -> {
                    ResponseWrapper.Success(it.value.map {it.toImageDomainModel()})
                }
                is ResponseWrapper.GenericError -> {
                    ResponseWrapper.GenericError(it.code,it.error)
                }
                is ResponseWrapper.NetworkError -> {
                    ResponseWrapper.NetworkError
                }
            }
        }
    }
}