package com.jukti.code4lyst.domain

import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.data.model.toBreedDomain
import com.jukti.code4lyst.domain.model.BreedDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetDogBreedsUseCase @Inject constructor(private val repository : DogRepository) {
    suspend fun execute():Flow<ResponseWrapper<List<BreedDomainModel>>>{
        return repository.getAllDogBreeds().map {
            when(it){
                is ResponseWrapper.Success -> {
                    ResponseWrapper.Success(it.value.map {it.toBreedDomain()})
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