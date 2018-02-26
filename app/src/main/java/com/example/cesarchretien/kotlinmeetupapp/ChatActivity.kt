package com.example.cesarchretien.kotlinmeetupapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

const val CAMERA_PERMISSION_REQUEST_CODE = 300
const val SIGN_IN_REQUEST_CODE = 200
const val CAMERA_REQUEST_CODE = 100
const val MAXIMUM_MESSAGES = 50
const val DATABASE_NAME = "chats"

class ChatActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser != null) {
            displayChatMessages()
        }
    }

    override fun onStart() {
        super.onStart()

        if (user() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE)
        }

        FirebaseAuth.getInstance().addAuthStateListener(this)
    }

    override fun onStop() {
        super.onStop()
        FirebaseAuth.getInstance().removeAuthStateListener(this)
    }

    private val query = FirebaseDatabase
            .getInstance()
            .reference
            .child(DATABASE_NAME)
            .limitToLast(MAXIMUM_MESSAGES)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fab.setOnClickListener {
            val messageText = editText.text.toString()

            if (messageText.isNotEmpty()) {
                query.ref
                        .push()
                        .setValue(ChatMessage(messageText, user()?.displayName
                                ?: "Unknown"))

                editText.clear()
            } else {
                startCamera()
            }
        }
    }

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //You have camera permission, so time to take a picture!
            startActivityForResult(Intent(this, CameraActivity::class.java), CAMERA_REQUEST_CODE)
        } else {
            //No camera permission, so you need to ask for it first.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                parentView.brieflyShowSnackbar("Explain it to me please.")
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startActivityForResult(Intent(this, CameraActivity::class.java), CAMERA_REQUEST_CODE)
            }
        }
    }

    private fun user(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            displayChatMessages()
        } else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val byteArrayExtra = data?.getByteArrayExtra("picture")
                val thumbnail: Bitmap? = BitmapFactory.decodeByteArray(byteArrayExtra, 0, byteArrayExtra?.size ?: 0)

                parentView.brieflyShowSnackbar("Image received ${if(thumbnail == null) "un" else ""}successfully")
            }
        }
    }

    private fun ByteArray.print(): String = fold("") { acc, byte -> acc + byte.toString() }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean = menu.inflate(R.menu.main_menu)

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.log_out) {
            AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        if (it.isSuccessful) finish()
                    }
        }

        return true
    }

    private fun displayChatMessages() {
        recyclerView.adapter = newAdapter()
    }

    private fun newAdapter(): RecyclerView.Adapter<ChatMessageHolder> {
        val options = FirebaseRecyclerOptions.Builder<ChatMessage>()
                .setLifecycleOwner(this)
                .setQuery(query, ChatMessage::class.java)
                .build()

        return ChatMessageAdapter(options).apply {
            registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
                override fun onItemRangeInserted(positionStart: Int, itemCount: Int) = recyclerView.smoothScrollToPosition(this@apply.itemCount)
            })
        }
    }

    private fun Menu?.inflate(@MenuRes menuRes: Int): Boolean {
        menuInflater.inflate(menuRes, this)
        return true
    }
}