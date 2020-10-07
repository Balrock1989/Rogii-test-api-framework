package com.data

import com.github.javafaker.Faker
import java.util.*
/*** Генератор случайных тестовых данных*/
object Faker {
    var faker = Faker(Locale(System.getProperty("config.locale")))
}