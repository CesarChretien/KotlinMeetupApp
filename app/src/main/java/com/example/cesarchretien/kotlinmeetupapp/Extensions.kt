package com.example.cesarchretien.kotlinmeetupapp

import android.support.annotation.ColorRes
import android.support.annotation.LayoutRes
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v7.widget.CardView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import java.nio.charset.StandardCharsets

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

fun ByteArray.encode(): String = String(this, StandardCharsets.ISO_8859_1)

fun String.decode(): ByteArray = this.toByteArray(StandardCharsets.ISO_8859_1)