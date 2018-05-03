package com.example.dmitrykurilov.vkfriendviewer.mvp.presenter

import com.arellomobile.mvp.InjectViewState
import com.arellomobile.mvp.MvpPresenter
import com.example.dmitrykurilov.vkfriendviewer.dao.TokenDao
import com.example.dmitrykurilov.vkfriendviewer.data.User
import com.example.dmitrykurilov.vkfriendviewer.mvp.view.FriendsView
import com.example.dmitrykurilov.vkfriendviewer.service.FriendsManager

@InjectViewState
class FriendsPresenter: MvpPresenter<FriendsView>() {

    private val friendsManager = FriendsManager()
    private lateinit var friends: List<User>
    private lateinit var token: String
    private val tokenDao = TokenDao()

    fun getFriends() {
        friends = friendsManager.getFriends(token)
        viewState.getFriends(friends)
    }

    fun showImage(position: Int) {
        viewState.showImage(friends[position])
    }

    fun setToken(token: String) {
        initToken(token)
        tokenDao.saveToken(token)
        viewState.onReady()
    }

    fun initToken(token: String) {
        this.token = token
    }

    fun authorise() {
        val t = tokenDao.getAllTokens()
        if (t.isEmpty()) {
            viewState.authorise()
        } else {
            token = t[0].token!!
            viewState.onReady()
        }
    }

    fun dismissPopup() {
        viewState.dismissPopup()
    }

    fun saveBitmapsOnLocal() {

    }
}