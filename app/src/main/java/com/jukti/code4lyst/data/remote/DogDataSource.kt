package com.jukti.code4lyst.data.remote

import com.jukti.code4lyst.api.NoConnectivityException
import com.jukti.code4lyst.api.DogApiService
import com.jukti.code4lyst.data.model.Breed
import com.jukti.code4lyst.data.model.Image
import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.di.IoDispatcher
import com.jukti.unrd.utilities.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

import javax.inject.Inject

class DogDataSource @Inject constructor(private val apiService: DogApiService,
                                        @IoDispatcher private val ioDispatcher: CoroutineDispatcher){

    suspend fun getAllBreeds(): ResponseWrapper<List<Breed>>{
        return safeApiCall(apiCall = { apiService.getAllBreeds()})
    }

    suspend fun getImagesByBreed(breedId:Int,limit:Int): ResponseWrapper<List<Image>>{
        return safeApiCall(apiCall = { apiService.getImagesByBreed(breedId,limit)})
    }

    suspend fun <T> safeApiCall(apiCall: suspend () -> T): ResponseWrapper<T> {
        return withContext(ioDispatcher) {
            try {
                ResponseWrapper.Success(apiCall.invoke())
            } catch (throwable: Throwable) {
                when (throwable) {
                    is NoConnectivityException -> ResponseWrapper.NetworkError
                    is IOException -> ResponseWrapper.NetworkError
                    is HttpException -> {
                        val code = throwable.code()
                        val msg = throwable.message()
                        val errorMsg = if (msg.isNullOrEmpty()) {
                            throwable.response()?.errorBody()?.toString()
                        } else {
                            msg
                        }
                        ResponseWrapper.GenericError(code, errorMsg)
                    }
                    else -> {
                        ResponseWrapper.GenericError(null, null)
                    }
                }
            }
        }
    }


    companion object {
        private const val TAG = "ClearScoreDataSource"
    }


}