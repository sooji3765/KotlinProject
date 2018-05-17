package com.example.user.myapplication.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass

@RealmClass
open class User: RealmObject(){

    @PrimaryKey
    open var num:Long = 0
    open var id:String? = null
    open var email:String? = null
    open var password1:String? = null
    open var password2:String? = null

}