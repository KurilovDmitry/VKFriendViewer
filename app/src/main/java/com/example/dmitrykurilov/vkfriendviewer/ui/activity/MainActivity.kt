package com.example.dmitrykurilov.vkfriendviewer.ui.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.dmitrykurilov.vkfriendviewer.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.regex.Pattern


class MainActivity : AppCompatActivity() {

    private val TAG = "VK.Auth"
    private val redirect_url = "https://oauth.vk.com/blank.html"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_layout_webview.settings.javaScriptEnabled = true
        login_layout_webview.clearCache(true)

        //Чтобы получать уведомления об окончании загрузки страницы
        login_layout_webview.webViewClient = VkWebViewClient()

        //otherwise CookieManager will fall with java.lang.IllegalStateException: CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()
        val cookieManager = CookieManager.getInstance()
        cookieManager.flush()
        cookieManager.removeAllCookies(null)

        val url = "https://oauth.vk.com/authorize?client_id=6458929&display=page&redirect_uri=https://oauth.vk.com/blank.html&scope=offline,friends&response_type=token&v=5.52"
        login_layout_webview.loadUrl(url)
    }

    inner class VkWebViewClient : WebViewClient() {

        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            parseUrl(url)
        }
    }

    private fun parseUrl(url: String?) {
        if (url == null)
            return

        if (url.startsWith(redirect_url)) {
            if (!url.contains("error=")) {
                val auth = parseRedirectUrl(url)
                val intent = Intent()
                intent.putExtra("token", auth[0])
                intent.putExtra("user_id", auth[1].toLong())
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
    }

    private fun parseRedirectUrl(url: String): Array<String> {
        //url is something like http://api.vkontakte.ru/blank.html#access_token=66e8f7a266af0dd477fcd3916366b17436e66af77ac352aeb270be99df7deeb&expires_in=0&user_id=7657164
        val accessToken = extractPattern(url, "access_token=(.*?)&")
        val userId = extractPattern(url, "user_id=(\\d*)")
        return arrayOf(accessToken, userId)
    }

    private fun extractPattern(string: String, pattern: String): String {
        val p = Pattern.compile(pattern)
        val m = p.matcher(string)
        return if (!m.find()) "" else m.toMatchResult().group(1)
    }
}