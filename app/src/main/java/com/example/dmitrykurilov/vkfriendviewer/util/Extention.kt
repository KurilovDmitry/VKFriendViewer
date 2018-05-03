package com.example.dmitrykurilov.vkfriendviewer.util

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dmitrykurilov.vkfriendviewer.data.User
import com.example.dmitrykurilov.vkfriendviewer.service.UserImageManager

fun User.getIconBitmap() = UserImageManager.getIcon(id, photo_50)
fun User.getWholeScreenImageBitmap() = UserImageManager.getWholeScreenImage(id, photo_200_orig)

fun ViewGroup.inflate(resource: Int, attachToRoot: Boolean = false) = LayoutInflater.from(context).inflate(resource, this, attachToRoot)