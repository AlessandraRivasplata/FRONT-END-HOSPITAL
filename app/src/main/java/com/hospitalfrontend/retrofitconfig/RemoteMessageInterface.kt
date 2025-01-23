package com.hospitalfrontend.retrofitconfig

import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.model.NurseResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface RemoteMessageInterface {
    @GET("/nurse/allnurses")
    suspend fun getAllNurses(): NurseResponse

    @Headers("Accept: application/json","Content-Type: application/json")
    @POST("nurse")
    suspend fun createNurse(nurse: Nurse): NurseResponse
}
