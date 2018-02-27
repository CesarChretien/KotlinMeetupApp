package com.example.cesarchretien.kotlinmeetupapp

import android.graphics.BitmapFactory
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_layout.*

/**
 * Created by cesarchretien on 14/02/2018.
 */
class MessageHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    infix fun populateWith(message: Message) {
        if (FirebaseAuth.getInstance().currentUser?.displayName == message.messageUser) {
            with(messageCardView) {
                setLayoutGravity(Gravity.END)
                setCardBackgroundColorByRes(R.color.colorActiveUser)
            }
        }

        userNameView.text = message.messageUser

        val messageView = when (message.messageType) {
            MessageType.TEXT -> TextView(containerView?.context).apply { text = message.messageText }
            MessageType.IMAGE -> ImageView(containerView?.context).apply {
                val imageByteArray = message.messageText.decode()
                setImageBitmap(BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size))
            }
        }.apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, 0, 1.0f)
        }

        messageContainerView.apply {
            if (childCount < 2) {
                addView(messageView)
            }
        }
    }
}