package com.hospitalfrontend.model

import com.google.gson.annotations.SerializedName

data class Room(
    val id: Int,
    @SerializedName("room_number") val roomNumber: Int,
    val floor: Int
)
data class RoomResponse(
    val data: List<Room>
)
