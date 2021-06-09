package com.example.squirrelwarehouse.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
//user나타내는 새로운 클래스 지정
class User(val uid: String, val username: String, val profileImageUrl: String) : Parcelable {
    constructor() : this("","","")
}