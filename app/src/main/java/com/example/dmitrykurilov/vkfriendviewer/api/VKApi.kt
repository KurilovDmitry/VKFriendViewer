package com.example.dmitrykurilov.vkfriendviewer.api

import com.example.dmitrykurilov.vkfriendviewer.data.FriendsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface VKApi {
    @GET("/method/friends.get")
    fun getFriends(
            @Query("v") v: String = "5.52",
            @Query("access_token") access_token: String = "240949c922aadb96228e53d9f93d4ae40ff8eefe9c532f3ff4be67afaa9e7459393a2c3685c77326a00f4",
            @Query("fields") fields: String = "photo_50,photo_200_orig"): Call<FriendsResponse>
}