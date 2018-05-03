package com.example.dmitrykurilov.vkfriendviewer.mvp.view

import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType
import com.example.dmitrykurilov.vkfriendviewer.data.User

@StateStrategyType(value = AddToEndSingleStrategy::class)
interface FriendsView: MvpView {

    fun getFriends(friends: List<User>)

    fun showImage(user: User)

    @StateStrategyType(value = OneExecutionStateStrategy::class)
    fun authorise()

    fun onReady()

    fun dismissPopup()
}