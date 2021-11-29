package com.jukti.code4lyst.api

import com.jukti.code4lyst.data.model.Breed
import com.jukti.code4lyst.data.model.Image
import retrofit2.http.GET
import retrofit2.http.Query

interface DogApiService {

        @GET("/v1/breeds/")
        suspend fun getAllBreeds() : List<Breed>

        @GET("/v1/images/search")
        suspend fun getImagesByBreed(@Query("breed_ids")breedId:Int,
                                     @Query("limit")limit:Int): List<Image>

}