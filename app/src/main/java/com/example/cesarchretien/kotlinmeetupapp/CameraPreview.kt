package com.example.cesarchretien.kotlinmeetupapp

import android.annotation.SuppressLint
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

@SuppressLint("ViewConstructor")
class CameraPreview(context: Context, attributeSet: AttributeSet? = null, val cameraInstance: Camera? = null) : SurfaceView(context, attributeSet), SurfaceHolder.Callback {

    init {
        holder?.apply {
            addCallback(this@CameraPreview)
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
        Log.d(TAG, "Surface has changed, (w, h) = ($width, $height)")
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

    inline fun onPictureTaken(crossinline action: (encodedImage: ByteArray) -> Unit) {
        cameraInstance?.takePicture(null, null, { data, _ ->
            action(data)
        })
    }

    private inline fun Camera.applyExceptionSafe(action: Camera.() -> Unit) = try {
        action()
    }
    catch (e: Exception) {
        Log.d(TAG, e.message)
    }
}