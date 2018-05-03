package com.example.dmitrykurilov.vkfriendviewer.api

import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class FriendsRestApi : FriendsApi {

    private val vkApi: VKApi

    init {
        val retorofit = Retrofit.Builder()
                .baseUrl("https://api.vk.com/")
                .addConverterFactory(MoshiConverterFactory.create())
                .build()

        vkApi = retorofit.create(VKApi::class.java)
    }

    override fun getFriends(token: String) = vkApi.getFriends(access_token = token)
}