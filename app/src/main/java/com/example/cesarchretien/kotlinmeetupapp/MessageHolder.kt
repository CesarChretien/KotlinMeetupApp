package com.example.cesarchretien.kotlinmeetupapp

import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.message_layout.*

/**
 * Created by cesarchretien on 14/02/2018.
 */
class MessageHolder(override val containerView: View?) : RecyclerView.ViewHolder(containerView), LayoutContainer {

    infix fun populateWith(chatMessage: Message) {
//        userNameView.text = chatMessage.messageUser
//        messageView.text = chatMessage.messageText

        if (FirebaseAuth.getInstance().currentUser?.displayName == ""/*chatMessage.messageUser*/) {
            messageCardView.apply {
                setLayoutGravity(Gravity.END)
                setCardBackgroundColorByRes(R.color.colorActiveUser)
            }
        }
    }
}