package com.models.general.dataObjects

import com.data.Faker
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.function.Consumer

@Serializable
data class UserDataModel(
//        @Required override var id: Int? = null,
        @Required var email: String = Faker.faker.internet().emailAddress(),
        @Required @SerialName("first_name") var firstName: String = Faker.faker.name().firstName(),
        @Required @SerialName("last_name") var lastName: String = Faker.faker.name().lastName(),
        @Required var avatar: String = Faker.faker.avatar().image(),
        var createdAt: String? = null

) : BaseModel<UserDataModel>() {

    override fun getBody(): String {
        return Json.encodeToString(serializer(), this)
    }

    override fun getSerializer(): KSerializer<UserDataModel> {
        return serializer()
    }

    constructor(builder: Consumer<UserDataModel>) : this() {
        builder.accept(this)
    }
}