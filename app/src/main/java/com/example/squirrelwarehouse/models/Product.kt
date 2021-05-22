package com.example.squirrelwarehouse.models

import com.google.firebase.firestore.GeoPoint

// 물건 정보
data class Product(var UserId : String? = null, var UserName : String? = null, var ProductName : String? = null, var Category : String? = null, var ProductDetail : String? = null,
                   var ImageURI : String? = null, var Deposit : String? = null, var RentalFee : String? = null, var UploadTime : String? = null, var Region : GeoPoint? = null)