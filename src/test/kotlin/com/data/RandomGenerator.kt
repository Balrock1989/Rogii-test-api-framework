package com.data

import net.bytebuddy.utility.RandomString

/*** Генератор случайных данных*/
interface RandomGenerator {
    fun getRandomString(count : Int) :String{
        return RandomString(count).nextString()
    }
}
