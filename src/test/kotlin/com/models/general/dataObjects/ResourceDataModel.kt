package com.models.general.dataObjects

import com.data.Faker
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.function.Consumer


@Serializable
data class ResourceDataModel(
        @Required var id: Int? = null,
        @Required var name: String = Faker.faker.company().name(),
        @Required var color: String = Faker.faker.color().name(),
        @Required @SerialName("pantone_value") var pantone: String = Faker.faker.address().buildingNumber(),
        @Required var year: Int = Faker.faker.number().randomDigitNotZero()
) {

    constructor(builder: Consumer<ResourceDataModel>) : this() {
        builder.accept(this)
    }
}

