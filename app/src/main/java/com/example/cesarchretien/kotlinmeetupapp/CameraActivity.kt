package com.example.cesarchretien.kotlinmeetupapp

import android.content.pm.PackageManager
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
        cameraView.brieflyShowSnackbar("You have ${if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) "a" else "no"} camera.")

        cameraButton.setOnClickListener {

        }
    }
}