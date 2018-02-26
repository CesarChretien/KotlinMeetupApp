package com.example.cesarchretien.kotlinmeetupapp

import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import java.io.IOException

/**
 * Created by cesarchretien on 23/02/2018.
 */
class CameraView(context: Context, attributeSet: AttributeSet? = null) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    private val cameraInstance: Camera? = try {
        Camera.open()
    }
    catch (e: Exception) {
        null
    }

    init {
        holder?.apply {
            addCallback(this@CameraView)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        with (holder) {
            this?.surface.also {
                cameraInstance?.applyIOSafe {
                    setPreviewDisplay(this@with)
                    startPreview()
                }
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        cameraInstance?.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        cameraInstance?.applyIOSafe {
            setPreviewDisplay(holder)
            startPreview()
        }
    }

    fun getPictureIntent() {
        cameraInstance?.takePicture(null, null, { data, camera ->
            val cameraResult = Intent()
            cameraResult
            this@CameraView.apply {

            }
        })
    }

    private fun Camera.applyIOSafe(action: Camera.() -> Unit) = try {
        action()
    }
    catch (ioe: IOException) {

    }
}