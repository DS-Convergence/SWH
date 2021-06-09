package com.example.squirrelwarehouse.models

data class UserModelFS (
        var uid : String? = null,
        var email : String? = null,
        var nickname : String? = null,
        var location : String? = null,
        var rating : Long? = 0,
        var userProPic : String? = null){}