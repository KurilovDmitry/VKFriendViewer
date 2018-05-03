package com.example.dmitrykurilov.vkfriendviewer.data

data class FriendsDataResponse(val count: Long,
                               val items: List<FriendsDataItemResponse>)