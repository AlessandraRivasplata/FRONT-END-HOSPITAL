package com.hospitalfrontend.retrofitconfig

import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.model.NurseResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query


interface RemoteMessageInterface {
    @GET("/nurse/allnurses")
    suspend fun getAllNurses(): NurseResponse
  
    @GET("/nurse/findnursebyname")
    suspend fun getNursesByName(@Query("name") name: String): Response<List<Nurse>>
}
