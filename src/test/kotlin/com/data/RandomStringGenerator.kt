package com.data

import net.bytebuddy.utility.RandomString

/*** Генератор случайных данных*/
interface RandomStringGenerator {
    fun getRandomString(count: Int): String {
        return RandomString(count).nextString()
    }
}
