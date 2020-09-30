package com


import com.api.data.RandomGenerator
import com.api.helpers.ApiRequestHelper
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite


open class BaseTest : ApiRequestHelper(), RandomGenerator {

    @BeforeSuite
    fun setupBeforeTests(){
        if (System.getProperty("logger").toBoolean()) {
            initOkHTTPClientWithLogger()
        } else {
            initOkHTTPClient()
        }
    }

    @AfterSuite
    fun clear(){

    }
}