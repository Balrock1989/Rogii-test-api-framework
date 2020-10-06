package com.models.general.dataObjects

import kotlinx.serialization.KSerializer

abstract class BaseModel<T : BaseModel<T>> {

    abstract fun getBody(): String
    abstract fun getSerializer(): KSerializer<T>
}