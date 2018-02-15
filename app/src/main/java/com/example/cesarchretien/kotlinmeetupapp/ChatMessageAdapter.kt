package com.example.cesarchretien.kotlinmeetupapp

import android.support.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

/**
 * Created by cesarchretien on 14/02/2018.
 */
class ChatMessageAdapter(val options: FirebaseRecyclerOptions<ChatMessage>) : FirebaseRecyclerAdapter<ChatMessage, ChatMessageHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = ChatMessageHolder(parent?.inflateChild(R.layout.message_layout))

    override fun onBindViewHolder(holder: ChatMessageHolder, position: Int, model: ChatMessage) = holder populateWith model

    private fun ViewGroup.inflateChild(@LayoutRes layoutRes: Int) = LayoutInflater.from(context).inflate(layoutRes, this, false)
}