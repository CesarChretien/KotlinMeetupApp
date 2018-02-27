package com.example.cesarchretien.kotlinmeetupapp

import java.util.*

/**
 * Created by cesarchretien on 08/02/2018.
 */
data class Message(
        val text: String = "",
        val user: String = "",
        val time: Long = Date().time,
        val type: MessageType = MessageType.TEXT
)

public enum class MessageType {
    TEXT, IMAGE
}