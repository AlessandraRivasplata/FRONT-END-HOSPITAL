package com.hospitalfrontend.retrofitconfig

import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.model.NurseResponse
import retrofit2.http.GET

interface RemoteMessageInterface {
    @GET("/nurse/allnurses")
    suspend fun getAllNurses(): NurseResponse
}