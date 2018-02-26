package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * Created by cesarchretien on 23/02/2018.
 */
class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        cameraView.brieflyShowSnackbar("You have ${if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) "a" else "no"} camera.")

        cameraButton.getPictureOnClick {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra("picture", it)
            })
            finish()
        }
    }

    private fun Button.getPictureOnClick(action: (ByteArray) -> Unit) {
        setOnClickListener {
            cameraView.onPictureTaken { action(it) }
        }
    }
}