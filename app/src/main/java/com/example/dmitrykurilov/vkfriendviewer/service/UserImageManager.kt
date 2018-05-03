package com.example.dmitrykurilov.vkfriendviewer.service

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import com.example.dmitrykurilov.vkfriendviewer.VKFriendViewerApplication.Companion.context
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.util.concurrent.ConcurrentHashMap

class UserImageManager {

    companion object {
        private var icCache = ConcurrentHashMap<Long, Bitmap>()
        private var fullScreenImageCache = ConcurrentHashMap<Long, Bitmap>()
        private val emptyBmp = getEmptyBitmap()

        init {
            loadImageFromLocal(CachePrefix.ICON.prefix)
            LoadImageFromLocal().execute(CachePrefix.FULLSCREEN.prefix)
        }

        fun getIcon(userId: Long, url: String): Bitmap {
            if (!icCache.containsKey(userId)) {
                SetInCache().execute(userId.toString(), url, CachePrefix.ICON.prefix)
                return emptyBmp
            }

            return icCache[userId] ?: emptyBmp
        }

        fun getWholeScreenImage(userId: Long, url: String): Bitmap {
            var bmp = fullScreenImageCache[userId]
            if (bmp == null) {
                bmp = LoadImageFromWebTask().execute(url).get() ?: return emptyBmp
                fullScreenImageCache[userId] = bmp
                SaveOnLocal().execute(Triple(userId, bmp, CachePrefix.FULLSCREEN.prefix))
            }

            return bmp
        }

        class SetInCache : AsyncTask<String, Unit, Unit>() {
            override fun doInBackground(vararg params: String?) {
                val bmp = getBitmapFromURL(params[1]!!) ?: return
                when (params[2]!!) {
                    CachePrefix.ICON.prefix -> icCache[params[0]!!.toLong()] = bmp
                    CachePrefix.FULLSCREEN.prefix -> fullScreenImageCache[params[0]!!.toLong()] = bmp
                }
                saveOnLocal(params[0]!!.toLong(), bmp, params[2]!!)
            }
        }

        class SaveOnLocal : AsyncTask<Triple<Long, Bitmap, String>, Unit, Unit>() {
            override fun doInBackground(vararg params: Triple<Long, Bitmap, String>?) {
                saveOnLocal(params[0]!!.first, params[0]!!.second, params[0]!!.third)
            }
        }

        class LoadImageFromWebTask : AsyncTask<String, Unit, Bitmap?>() {

            override fun doInBackground(vararg params: String?) = getBitmapFromURL(params[0]!!)
        }

        class LoadImageFromLocal : AsyncTask<String, Unit, Unit>() {
            override fun doInBackground(vararg params: String?) {
                loadImageFromLocal(params[0]!!)
            }
        }

        private fun getEmptyBitmap(): Bitmap {
            val w = 50
            val h = 50
            val conf = Bitmap.Config.ARGB_8888 // see other conf types
            return Bitmap.createBitmap(w, h, conf) // this creates a MUTABLE bitmap
        }

        fun loadImageFromLocal(prefix: String) {
            val path = context.filesDir.toString() + "/$prefix"
            val dir = File(path)
            val files = dir.listFiles() ?: return
            when (prefix) {
                CachePrefix.ICON.prefix -> setInCache(files, icCache)

                CachePrefix.FULLSCREEN.prefix -> setInCache(files, fullScreenImageCache)
            }
        }

        private fun setInCache(files: Array<File>, cache: ConcurrentHashMap<Long, Bitmap>) {
            for (file in files) {
                cache[file.name.toLong()] = BitmapFactory.decodeStream(FileInputStream(file))
            }
        }

        fun getBitmapFromURL(src: String): Bitmap? {
            return try {
                val url = java.net.URL(src)
                val connection = url
                        .openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }

        fun saveOnLocal(t: Long, u: Bitmap, prefix: String) {
            val path = context.filesDir.toString() + "$prefix"
            val file = File(path)
            file.mkdirs()
            val fOut = FileOutputStream("$path/$t")
            fOut.use {
                u.compress(Bitmap.CompressFormat.PNG, 100, it)
            }
            fOut.close()
        }
    }

    enum class CachePrefix(val prefix: String) {
        ICON("/icon"),
        FULLSCREEN("/fullscreen")
    }
}