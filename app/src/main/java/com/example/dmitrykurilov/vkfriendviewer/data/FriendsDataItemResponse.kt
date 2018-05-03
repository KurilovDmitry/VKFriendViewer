package com.example.dmitrykurilov.vkfriendviewer.data

data class FriendsDataItemResponse(
        val id: Long,
        val first_name: String,
        val last_name: String,
        val photo_50: String,
        val photo_200_orig: String,
        val online: Int
)