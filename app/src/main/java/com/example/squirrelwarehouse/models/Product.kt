package com.example.squirrelwarehouse.models

import com.google.firebase.firestore.GeoPoint

// 물건 정보
data class Product(var userId : String? = null, var userName : String? = null, var productName : String? = null, var category : String? = null, var categoryHobby : String? = null, var productDetail : String? = null,
                   var imageURI : String? = null, var deposit : String? = null, var rentalFee : String? = null, var uploadTime : String? = null, var region : GeoPoint? = null, var status : String? = null)