package com.example.cesarchretien.kotlinmeetupapp

import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

/**
 * Created by cesarchretien on 14/02/2018.
 */
private const val TAG = "MessageAdapter"
private const val TEXT = 1
private const val IMAGE = 2

class MessageAdapter(private val options: FirebaseRecyclerOptions<Message>) : FirebaseRecyclerAdapter<Message, MessageHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MessageHolder = when(viewType) {
        TEXT -> ChatMessageHolder(parent?.inflateChild(R.layout.chat_message_layout))
        IMAGE -> ImageMessageHolder(parent?.inflateChild(R.layout.image_message_layout))
        else -> throw Exception("Unknown view type.")
    }

    override fun onBindViewHolder(holder: MessageHolder, position: Int, model: Message) = holder populateWith model

    override fun getItemViewType(position: Int) = when (options.snapshots[position].type) {
        MessageType.TEXT -> TEXT
        MessageType.IMAGE -> IMAGE
    }
}