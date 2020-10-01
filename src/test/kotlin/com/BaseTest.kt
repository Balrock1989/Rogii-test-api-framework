package com

import com.api.Generator
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeMethod
import java.lang.reflect.Method

open class BaseTest : Generator() {

    /*** Логирование названия теста*/
    @BeforeMethod
    open fun handleTestMethodName(method: Method) {
        logger.info("***************** Run Test : " + method.name.toString() + " *****************")
    }

    /*** Очистка тестовых данных*/
    @AfterSuite
    fun clear() {
    }
}