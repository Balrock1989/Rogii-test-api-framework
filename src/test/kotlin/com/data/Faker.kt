package com.data

import com.github.javafaker.Faker
import java.util.*

object Faker {
    var faker = Faker(Locale(System.getProperty("config.locale")))
}