package com.data

import net.bytebuddy.utility.RandomString

/*** Генератор случайной строки с цифрами*/
interface RandomStringGenerator {
    fun getRandomString(count: Int): String {
        return RandomString(count).nextString()
    }
}
