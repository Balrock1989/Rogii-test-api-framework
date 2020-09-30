package com


import org.testng.annotations.AfterSuite
import org.testng.annotations.BeforeSuite

open class BaseTest{


    @BeforeSuite
    fun prepare(){
    //TODO добавить профиль на включение и отключение логирования
    }

    @AfterSuite
    fun clear(){

    }
}