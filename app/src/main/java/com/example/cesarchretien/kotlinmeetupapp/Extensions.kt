package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.hardware.Camera
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.Base64
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import java.io.ByteArrayOutputStream

/**
 * Created by cesarchretien on 21/02/2018.
 */
fun CardView.setCardBackgroundColorByRes(@ColorRes colorRes: Int) = this.setCardBackgroundColor(ContextCompat.getColor(context, colorRes))

fun View.brieflyShowSnackbar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()

fun TextView.clear() {
    this.text = ""
}

fun View.setLayoutGravity(gravity: Int) {
    if (this is FrameLayout) {
        (layoutParams as FrameLayout.LayoutParams).gravity = gravity
    }
    else {
        Log.e("View.setLayoutGravity", "This view is not a FrameLayout.")
    }
}

fun ViewGroup.inflateChild(@LayoutRes layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)

fun ByteArray.encode(): String = Base64.encodeToString(this, Base64.DEFAULT)

fun String.decode(): ByteArray = Base64.decode(this, Base64.DEFAULT)

fun Bitmap.rotate(degrees: Float, filter: Boolean = false): Bitmap {
    val rotationMatrix = Matrix().apply {
        setRotate(degrees)
    }

    return Bitmap.createBitmap(this, 0, 0, width, height, rotationMatrix, filter)
}

fun Activity.rotateAndCompress(byteArray: ByteArray): ByteArray {
    val smallBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size).let {

        val degrees = getCorrectOrientation(this, 0).toFloat()
        val rotatedBitmap = if (degrees in 1..359) it.rotate(degrees) else it
        val (screenWidth, _) = getScreenDimensions()
        val ratio = screenWidth.toDouble() / (2 * rotatedBitmap.width)

        rotatedBitmap
                .run {
                    val scaledWidth = (width * ratio).toInt()
                    val scaledHeight = (height * ratio).toInt()
                    Bitmap.createScaledBitmap(this, scaledWidth, scaledHeight, true)
                }
    }

    return ByteArrayOutputStream().use {
        smallBitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        it.toByteArray()
    }
}

typealias ScreenDimensions = Pair<Int, Int>

fun Activity.getScreenDimensions(): ScreenDimensions = DisplayMetrics().let {
    windowManager.defaultDisplay.getMetrics(it)
    it.widthPixels to it.heightPixels
}

fun <T : Any> T.log(any: Any): Int = Log.d(this::class.java.simpleName, any.toString())

operator fun Double.times(layoutParams: FrameLayout.LayoutParams): FrameLayout.LayoutParams = layoutParams.apply {
    width = (this@times * width).toInt()
    height = (this@times * height).toInt()
}

fun getCorrectOrientation(activity: Activity, cameraId: Int): Int {
    val info = Camera.CameraInfo()
    Camera.getCameraInfo(cameraId, info)

    val rotation = activity.windowManager.defaultDisplay.rotation
    val degrees = when (rotation) {
        Surface.ROTATION_0 -> 0
        Surface.ROTATION_90 -> 90
        Surface.ROTATION_180 -> 180
        Surface.ROTATION_270 -> 270
        else -> throw Exception("Unknown rotation")
    }

    return when {
        isEmulator() -> 0
        info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT -> {
            val uncompensatedResult = (info.orientation + degrees) % 360
            (360 - uncompensatedResult) % 360 // compensate the mirror
        }
        else -> (info.orientation - degrees + 360) % 360
    }
}

//Good chance is you're running this in an emulator.
fun isEmulator() = Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.contains("Android SDK built for x86")