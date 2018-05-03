package com.example.dmitrykurilov.vkfriendviewer.data

import com.activeandroid.Model
import com.activeandroid.annotation.Column
import com.activeandroid.annotation.Table

@Table(name = "Token")
data class Token(
        @Column(name = "token")
        var token: String? = null
) : Model()