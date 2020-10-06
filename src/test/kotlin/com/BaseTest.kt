package com

import com.data.DataGenerator
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeMethod
import java.lang.reflect.Method

open class BaseTest : DataGenerator() {

    /*** Логирование названия теста*/
    @BeforeMethod(description = "Логирование названия тестового метода")
    open fun handleTestMethodName(method: Method) {
        if (System.getProperty("config.logger").toBoolean())
            logger.info("***************** Run Test: " + method.declaringClass.name.toString() + "." + method.name.toString() + " *****************")
    }

    /*** Очистка тестовых данных*/
    @AfterSuite(description = "Очистка тестовых данных")
    fun clear() {
        cleanObjects()
    }
}