package com


import com.api.Generator
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite


open class BaseTest : Generator() {

    @BeforeSuite
    fun setupBeforeTests() {
        if (System.getProperty("logger").toBoolean()) {
            initOkHTTPClientWithLogger()
        } else {
            initOkHTTPClient()
        }
    }

    @AfterSuite
    fun clear() {

    }
}