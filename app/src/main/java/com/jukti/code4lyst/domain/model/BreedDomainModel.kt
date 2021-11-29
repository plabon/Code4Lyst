package com.jukti.code4lyst.domain.model

data class BreedDomainModel(val bred_for: String?,
                            val breed_group: String?,
                            val description: String?,
                            val heightDomainModel: HeightDomainModel?,
                            val history: String?,
                            val id: Int,
                            val life_span: String?,
                            val name: String?,
                            val origin: String?,
                            val temperament: String?,
                            val weightDomainModel: WeightDomainModel?){
    override fun toString(): String {
        return if(this.name !=null) {
            this.name
        } else ""
    }
}
