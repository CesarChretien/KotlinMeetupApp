package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration.ORIENTATION_LANDSCAPE
import android.content.res.Configuration.ORIENTATION_PORTRAIT
import android.hardware.Camera
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.activity_camera.*

/**
 * Created by cesarchretien on 23/02/2018.
 */
class CameraActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)
        cameraView.brieflyShowSnackbar("You have ${if (baseContext.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)) "a" else "no"} camera.")

        val cameraInstance: Camera? = try {
            log("Camera creation!")
            Camera.open()
        }
        catch (e: Exception) {
            log("Failed to create camera instance...")
            null
        }

        cameraInstance?.setDisplayOrientation(this, 0)

        val preview = buildCameraPreview(cameraInstance)

        cameraButton.setOnClickListener {
            preview.onPictureTaken {
                val resultIntent = Intent().apply {
                    putExtra("picture", it)
                }

                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            }
        }

        cameraContainer.addView(preview)
    }

    private fun buildCameraPreview(cameraInstance: Camera?): CameraPreview {
        return CameraPreview(this, cameraInstance = cameraInstance).apply {
            cameraInstance?.parameters?.previewSize?.let {
                val (screenWidth, screenHeight) = this@CameraActivity.screenDimensions()

                //The camera width/height from the preview does not account for orientation, so we're fixing that here.
                val (actualCameraWidth, actualCameraHeight) = when (resources.configuration.orientation) {
                    ORIENTATION_LANDSCAPE -> it.width to it.height
                    ORIENTATION_PORTRAIT -> it.height to it.width
                    else -> throw Exception("Unknown orientation")
                }

                val ratio = Math.min(screenWidth.toDouble() / actualCameraWidth, screenHeight.toDouble() / actualCameraHeight)
                layoutParams = ratio * FrameLayout.LayoutParams(actualCameraWidth, actualCameraHeight, Gravity.CENTER)
            }
        }
    }
}