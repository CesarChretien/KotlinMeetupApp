package com.example.cesarchretien.kotlinmeetupapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * Created by cesarchretien on 23/02/2018.
 */
class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        sendButton.setOnClickListener {
            TODO("""
                When we press the send button, we should set the result of this activity a whatever is in the messageBox.
                Remember to specify your tag/key for the extra information you're going to send back!
                """)
        }
    }
}