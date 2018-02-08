package com.example.cesarchretien.kotlinmeetupapp

import java.util.*

/**
 * Created by cesarchretien on 08/02/2018.
 */
data class ChatMessage(val messageText: String, val messageUser: String, val messageTime: Long = Date().time)