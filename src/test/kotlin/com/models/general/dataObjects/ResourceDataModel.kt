package com.models.general.dataObjects

import com.data.Faker
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.function.Consumer


@Serializable
data class ResourceDataModel(
        @Required var name: String = Faker.faker.color().name(),
        @Required var color: String = Faker.faker.color().hex(),
        @Required @SerialName("pantone_value") var pantone: String = Faker.faker.number().numberBetween(10, 99).toString() + "-" + Faker.faker.number().numberBetween(1000, 9999),
        @Required var year: Int = Faker.faker.number().numberBetween(1900, 2200),
        var createdAt: String? = null

) : BaseModel<ResourceDataModel>() {

    override fun getBody(): String {
        return Json.encodeToString(serializer(), this)
    }

    override fun getSerializer(): KSerializer<ResourceDataModel> {
        return serializer()
    }

    constructor(builder: Consumer<ResourceDataModel>) : this() {
        builder.accept(this)
    }
}

