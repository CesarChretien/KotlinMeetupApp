package com.example.cesarchretien.kotlinmeetupapp

import android.content.Context
import android.hardware.Camera
import android.util.AttributeSet
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView

/**
 * Created by cesarchretien on 23/02/2018.
 */
private const val TAG = "CameraViewTag"

class CameraView(context: Context, attributeSet: AttributeSet? = null) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    val cameraInstance: Camera? by lazy {
        try {
            Log.d(TAG, "Camera creation!")
            Camera.open()
        }
        catch (e: Exception) {
            Log.d(TAG, "Failed to create camera instance...")
            null
        }
    }

    init {
        holder?.apply {
            addCallback(this@CameraView)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(TAG, "Surface has changed.")
        with(holder) {
            this?.surface.apply {
                cameraInstance?.applyExceptionSafe {
                    stopPreview()
                    setPreviewDisplay(holder)
                    startPreview()
                }
            }
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        Log.d(TAG, "Surface is destroyed")
        cameraInstance?.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        Log.d(TAG, "Surface is created.")
        cameraInstance?.applyExceptionSafe {
            setPreviewDisplay(holder)
            startPreview()
        }
    }

    inline fun onPictureTaken(crossinline action: (ByteArray) -> Unit) {
        cameraInstance?.takePicture(null, null, { data, _ ->
            Log.d("DataArray", data.joinToString { "$it" })
            action(data) })
    }

    private inline fun Camera.applyExceptionSafe(action: Camera.() -> Unit) = try {
        action()
    }
    catch (e: Exception) {
        Log.d(TAG, e.message)
    }

    private fun log(any: Any): Int = Log.d(TAG, any.toString())
}