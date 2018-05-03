package com.example.dmitrykurilov.vkfriendviewer.api

import com.example.dmitrykurilov.vkfriendviewer.data.FriendsResponse
import com.example.dmitrykurilov.vkfriendviewer.data.User
import retrofit2.Call

interface FriendsApi {
    fun getFriends(token: String): Call<FriendsResponse>
}