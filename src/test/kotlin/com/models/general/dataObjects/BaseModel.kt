package com.models.general.dataObjects

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Required
import kotlinx.serialization.Serializable

@Serializable
abstract class BaseModel<T : BaseModel<T>> {
    @Required open var id: Int? = null

    abstract fun getBody(): String
    abstract fun getSerializer(): KSerializer<T>
}