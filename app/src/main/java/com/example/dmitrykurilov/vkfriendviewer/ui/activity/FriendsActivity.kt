package com.example.dmitrykurilov.vkfriendviewer.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.example.dmitrykurilov.vkfriendviewer.R
import com.example.dmitrykurilov.vkfriendviewer.data.User
import com.example.dmitrykurilov.vkfriendviewer.mvp.presenter.FriendsPresenter
import com.example.dmitrykurilov.vkfriendviewer.mvp.view.FriendsView
import com.example.dmitrykurilov.vkfriendviewer.ui.adapter.FriendsAdapter
import com.example.dmitrykurilov.vkfriendviewer.util.getWholeScreenImageBitmap
import kotlinx.android.synthetic.main.friend_list_layout.*
import android.widget.LinearLayout
import android.view.*
import android.widget.PopupWindow
import android.view.Gravity



class FriendsActivity : MvpAppCompatActivity(), FriendsView {

    @InjectPresenter
    lateinit var presenter: FriendsPresenter
    private lateinit var mIncreaseAnimation: Animation
    private lateinit var popupWindow: PopupWindow
    private lateinit var popupView: View
    private lateinit var popupImageView: ImageView

    private val AUTHORISE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.friend_list_layout)

        friend_list_layout_rv_friends.adapter = initAdapter()
        initPopupView()

        mIncreaseAnimation = AnimationUtils.loadAnimation(this, R.anim.increase)

        presenter.authorise()
    }

    override fun getFriends(friends: List<User>) {
        (friend_list_layout_rv_friends.adapter as FriendsAdapter).addFriends(friends)
    }

    override fun showImage(user: User) {
        popupImageView.setImageBitmap(user.getWholeScreenImageBitmap())

        // show the popup window
        friends_layout.post({
            popupWindow.showAtLocation(friends_layout, Gravity.CENTER, 0, 0)
            popupImageView.startAnimation(mIncreaseAnimation)
        })
    }

    private fun initPopupView() {
        // inflate the layout of the popup window
        popupView = layoutInflater.inflate(R.layout.popup_layout, null) as View

        popupImageView = popupView.findViewById(R.id.popup_image)

        // create the popup window with View, Width, Height and Focusable
        popupWindow = PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, false)

        popupView.setOnTouchListener { _, _->
            presenter.dismissPopup()
            true
        }
    }

    override fun dismissPopup() {
        friends_layout.post({
            if (popupWindow.isShowing) popupWindow.dismiss()
        })
    }

    override fun onBackPressed() {
        when {
            popupWindow.isShowing -> presenter.dismissPopup()
            else -> {
                presenter.saveBitmapsOnLocal()
                super.onBackPressed()
            }
        }
    }

    private fun loadFriends() {
        presenter.getFriends()
    }

    private fun initAdapter(): FriendsAdapter {
        val onIconClickListener = object : FriendsAdapter.OnIconClickListener {
            override fun onClick(position: Int) {
                presenter.showImage(position)
            }
        }

        return FriendsAdapter(onIconClickListener)
    }

    override fun onReady() {
        loadFriends()
    }

    override fun authorise() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivityForResult(intent, AUTHORISE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (requestCode == AUTHORISE_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                presenter.setToken(data.getStringExtra("token"))
            }
        }
    }

    override fun onPause() {
        popupWindow.dismiss()
        super.onPause()
    }
}