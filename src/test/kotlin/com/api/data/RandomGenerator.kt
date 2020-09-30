package com.api.data

import net.bytebuddy.utility.RandomString


interface RandomGenerator {
    fun getRandomString(count : Int) :String{
        return RandomString(count).nextString()
    }
}
