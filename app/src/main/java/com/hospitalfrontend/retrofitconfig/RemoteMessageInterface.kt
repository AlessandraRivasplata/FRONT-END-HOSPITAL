package com.hospitalfrontend.retrofitconfig

import com.hospitalfrontend.model.Care
import com.hospitalfrontend.model.CareResponse
import com.hospitalfrontend.model.DiagnosisResponse
import com.hospitalfrontend.model.Nurse
import com.hospitalfrontend.model.NurseResponse
import com.hospitalfrontend.model.PatientResponse
import com.hospitalfrontend.model.PatientResponseById
import com.hospitalfrontend.model.RemoteMessage
import com.hospitalfrontend.model.RoomResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import java.sql.Wrapper


interface RemoteMessageInterface {
    @GET("/nurse/allnurses")
    suspend fun getAllNurses(): NurseResponse
  
    @GET("/nurse/findnursebyname")
    suspend fun getNursesByName(@Query("name") name: String): Response<List<Nurse>>

    @GET("/nurse/{id}")
    suspend fun getNurseById(@Path("id") id: Int): Response<Nurse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("/nurse")
    suspend fun createNurse(@Body nurse: Nurse): Response<RemoteMessage>

    @POST("/nurse/login")
    suspend fun login(
        @Query("nurseNumber") nurseNumber: Int
    ): Response<Nurse>

    @DELETE("nurse/{id}")
    suspend fun deleteNurseById(@Path("id") id: Int): Response<Unit>

    @PUT("/nurse/{id}")
    suspend fun updateNurse(
        @Path("id") id: Int,
        @Query("name") name: String,
        @Query("username") username: String,
        @Query("password") password: String
    ): Response<Unit>

    @GET("/room/all")
    suspend fun getAllRooms(): Response<RoomResponse>

    @GET("/patient/byroomnumber/{roomNumber}")
    suspend fun getAllPatientsByRoomNumber(@Path("roomNumber") roomNumber: Int): Response<PatientResponse>

    @GET("/patient/{id}")
    suspend fun getPatientById(@Path("id") id: Int): Response<PatientResponseById>

    @GET("/diagnosis/{id}")
    suspend fun getDiagnosisByPatientId(@Path("id") id: Int): Response<DiagnosisResponse>

    @GET("care/bypatientid/{id}")
    suspend fun getCaresByPatientId(@Path("id") id: Int): Response<CareResponse>

    @GET("/care/{id}")
    suspend fun getCareById(@Path("id") id: Int): Response<Care>

}
