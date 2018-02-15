package com.example.cesarchretien.kotlinmeetupapp

import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_layout.*

/**
 * Created by cesarchretien on 14/02/2018.
 */
class ChatMessageHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    infix fun populateWith(chatMessage: ChatMessage) {
        userNameView.text = chatMessage.messageUser
        messageView.text = chatMessage.messageText

        if (FirebaseAuth.getInstance().currentUser?.displayName == chatMessage.messageUser) {
            messageCardView.apply {
                setLayoutGravity(Gravity.END)
                setCardBackgroundColorByRes(R.color.colorActiveUser)
            }
        }
    }

    private fun View.setLayoutGravity(gravity: Int) {
        if (this is FrameLayout) {
            (layoutParams as FrameLayout.LayoutParams).gravity = gravity
        }
        else {
            Log.e("View.setLayoutGravity", "This view is not a FrameLayout.")
        }
    }

    private fun CardView.setCardBackgroundColorByRes(@ColorRes colorRes: Int) = this.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
}