package com.example.dmitrykurilov.vkfriendviewer.service

import android.os.AsyncTask
import android.util.Log
import com.example.dmitrykurilov.vkfriendviewer.api.FriendsRestApi
import com.example.dmitrykurilov.vkfriendviewer.data.User

class FriendsManager(private val api: FriendsRestApi = FriendsRestApi()) {

    fun getFriends(token: String): List<User> {
        val result = RetrieveFeedTask().execute(token)
        return result.get()
    }

    inner class RetrieveFeedTask : AsyncTask<String, Unit, List<User>>() {

        override fun doInBackground(vararg params: String?): List<User> {
            try {
                val callResponse = api.getFriends(params[0]!!)
                val response = callResponse.execute()

                if (response.isSuccessful) {
                    val response = response.body()!!
                    val friendsResponse = response.response
                    val friendDataResponse = friendsResponse.items
                    val userList = friendDataResponse.map { User(it.id, it.first_name, it.last_name, it.photo_50, it.photo_200_orig, it.online) }
                    return userList
                } else {
                    Log.e("VKPROBLEM", response.message())
                }

                return listOf()
            } catch (e: Exception) {
                Log.e("VKPROBLEM", e.toString())
                return listOf()
            }
        }
    }
}