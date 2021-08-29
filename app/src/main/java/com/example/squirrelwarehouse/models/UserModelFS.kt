package com.example.squirrelwarehouse.models

data class UserModelFS (
        var uid : String? = null,
        var email : String? = null,
        var nickname : String? = null,
        var introduce : String? = null,
        var rating : Float? = 0.0F,
        var ratingCnt : Int? = 0,
        var userProPic : String? = null){}

//TODO location 지우기(유저 위치 정보) 뷰에서도 없애기