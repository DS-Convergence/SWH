package com.example.squirrelwarehouse

import com.example.squirrelwarehouse.Repo.Companion.CONTENT_TYPE
import com.example.squirrelwarehouse.Repo.Companion.SERVER_KEY
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotiAPI {
    //서버 키와 보낼 형식을 헤더에 넣는다. (json)
    /*
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
        @Body notification: PushNotification
    ): Response<ResponseBody>*/

    //서버 키와 보낼 형식을 헤더에 넣는다. (json)
    @Headers("Authorization: key=$SERVER_KEY", "Content-Type:$CONTENT_TYPE")
    @POST("fcm/send")
    suspend fun postNotification(
            @Body notification: PushNotification
    ): Response<ResponseBody>

}

