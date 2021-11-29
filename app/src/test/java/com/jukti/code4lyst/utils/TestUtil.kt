package com.jukti.clearscoredemo.utils

import com.jukti.code4lyst.data.model.ResponseWrapper
import com.jukti.code4lyst.domain.model.BreedDomainModel
import com.jukti.code4lyst.domain.model.HeightDomainModel
import com.jukti.code4lyst.domain.model.ImageDomainModel
import com.jukti.code4lyst.domain.model.WeightDomainModel


object TestUtil {

    fun createBreedResponseWrapper(): ResponseWrapper<List<BreedDomainModel>> {
        return ResponseWrapper.Success(createDogBreedTestResponse())
    }

    fun createDogBreedTestResponse() : List<BreedDomainModel> {
        var breedList: List<BreedDomainModel> = emptyList()
        for (i in 1..10) {
            breedList.toMutableList().add(createBreedDomainModel())
        }
        return breedList
    }

    fun createBreedDomainModel(): BreedDomainModel{
        return BreedDomainModel("Small rodent hunting, lapdog","Toy","Loyal, Independent, Intelligent, Brave",
            createHeightDomainModelTest(),"",1,"12-15years","Affenpinscher","America","Stubborn, Curious, Playful, Adventurous, Active, Fun-loving",
            createWeightDomainModelTest())
    }

    fun createWeightDomainModelTest(): WeightDomainModel {
        return WeightDomainModel("10-12","30-40")
    }

    fun createHeightDomainModelTest(): HeightDomainModel {
        return HeightDomainModel("10-12","30-40")
    }


    fun createImageResponseWrapper(): ResponseWrapper<List<ImageDomainModel>> {
        return ResponseWrapper.Success(createDogImagesTestResponse())
    }

    fun createDogImagesTestResponse() : List<ImageDomainModel> {
        var imageList: List<ImageDomainModel> = emptyList()
        for (i in 1..10) {
            imageList.toMutableList().add(createImageDomainModelTest())
        }
        return imageList
    }

    fun createImageDomainModelTest(): ImageDomainModel {
        return ImageDomainModel(200,"P44Wk6wSj","https://cdn2.thedogapi.com/images/P44Wk6wSj.jpg",200)
    }

}