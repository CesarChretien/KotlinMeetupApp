package com.example.cesarchretien.kotlinmeetupapp

import java.util.*

/**
 * Created by cesarchretien on 08/02/2018.
 */
sealed class Message

data class ChatMessage(
        val messageText: String = "",
        val messageUser: String = "",
        val messageTime: Long = Date().time
) : Message()

data class ImageMessage(
        val byteArray: ByteArray = byteArrayOf(),
        val messageUser: String = "",
        val messageTime: Long = Date().time
) : Message()