package com.example.dmitrykurilov.vkfriendviewer.dao

import com.activeandroid.query.Delete
import com.activeandroid.query.Select
import com.example.dmitrykurilov.vkfriendviewer.data.Token

class TokenDao {
    fun getAllTokens() = Select().from(Token::class.java).execute<Token>()

    fun saveToken(token: String) {
        cleanTokens()
        val token = Token(token)
        token.save()
    }

    fun cleanTokens() {
        Delete().from(Token::class.java).execute<Token>()
    }
}