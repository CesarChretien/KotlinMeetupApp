package com.example.cesarchretien.kotlinmeetupapp

import java.util.*

/**
 * Created by cesarchretien on 08/02/2018.
 */
data class Message(
        val messageText: String = "",
        val messageUser: String = "",
        val messageTime: Long = Date().time,
        val messageType: MessageType = MessageType.TEXT
                  )

enum class MessageType {
    TEXT, IMAGE
}