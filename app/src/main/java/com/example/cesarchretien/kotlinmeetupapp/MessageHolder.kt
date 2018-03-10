package com.example.cesarchretien.kotlinmeetupapp

import android.graphics.BitmapFactory
import android.support.annotation.ColorRes
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.chat_message_layout.*
import kotlinx.android.synthetic.main.image_message_layout.*

/**
 * Created by cesarchretien on 14/02/2018.
 */
abstract class MessageHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    abstract infix fun populateWith(message: Message)

    fun setOwner(messageCardView: CardView, userNameView: TextView, message: Message) {

        with(messageCardView) {
            val isOwner = FirebaseAuth.getInstance().currentUser?.displayName == message.user
            isOwner.let {
                setLayoutGravity(if (it) Gravity.END else Gravity.START)
                setCardBackgroundColorByRes(if (it) R.color.colorActiveUser else R.color.cardview_light_background)
            }
        }

        userNameView.text = message.user
    }

    private fun CardView.setCardBackgroundColorByRes(@ColorRes colorRes: Int) = this.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))
}

class ChatMessageHolder(override val containerView: View?) : MessageHolder(containerView), LayoutContainer {

    override fun populateWith(message: Message) {
        setOwner(chatMessageCardView, chatUserNameView, message)
        chatUserMessageText.text = message.text
    }
}

class ImageMessageHolder(override val containerView: View?) : MessageHolder(containerView), LayoutContainer {

    override fun populateWith(message: Message) {
        setOwner(imageMessageCardView, imageUserNameView, message)
        val imageBytes = message.text.decode()
        imageUserMessageImage.setImageBitmap(BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size))
    }
}