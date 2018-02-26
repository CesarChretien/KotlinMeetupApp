package com.example.cesarchretien.kotlinmeetupapp

import android.view.ViewGroup
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions

/**
 * Created by cesarchretien on 14/02/2018.
 */
class MessageAdapter(val options: FirebaseRecyclerOptions<Message>) : FirebaseRecyclerAdapter<Message, MessageHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int) = MessageHolder(parent?.inflateChild(R.layout.message_layout))

    override fun onBindViewHolder(holder: MessageHolder, position: Int, model: Message) = holder populateWith model
}