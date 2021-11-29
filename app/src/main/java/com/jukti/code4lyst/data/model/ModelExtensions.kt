package com.jukti.code4lyst.data.model

import com.jukti.code4lyst.domain.model.BreedDomainModel
import com.jukti.code4lyst.domain.model.HeightDomainModel
import com.jukti.code4lyst.domain.model.ImageDomainModel
import com.jukti.code4lyst.domain.model.WeightDomainModel


fun Breed.toBreedDomain() = BreedDomainModel(
    bred_for = bred_for,
    breed_group = breed_group,
    description = description,
    history = history,
    id = id,
    life_span = life_span,
    name = name,
    temperament = temperament,
    origin = origin,
    heightDomainModel = height?.toHeightDomain(),
    weightDomainModel = weight?.toWeightDomain()
)

fun Height.toHeightDomain() = HeightDomainModel(
    imperial = imperial,
    metric =metric
)

fun Weight.toWeightDomain() = WeightDomainModel(
    imperial = imperial,
    metric =metric
)

fun Image.toImageDomainModel() = ImageDomainModel(
    id = id,
    width = width,
    height = height,
    url = url
)