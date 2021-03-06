package com.example.cesarchretien.kotlinmeetupapp

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
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

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        editText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val editTextIsEmpty = s == null || s.isEmpty()
                fab.setImageResource(if (editTextIsEmpty) R.drawable.ic_photo_camera_white_24dp else R.drawable.ic_send_white_24dp)
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //do nothing
            }
        })

        fab.setOnClickListener {
            val messageText = editText.text.toString()

            if (messageText.isNotEmpty()) {
                sendMessage(Message(messageText, userName()))
                editText.clear()
            }
            else {
                startCamera()
            }
        }
    }

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser != null) {
            displayChatMessages()
        }
    }

    override fun onStart() {
        super.onStart()
        FirebaseAuth.getInstance().addAuthStateListener(this)

        if (user() == null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE)
        }
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

    private fun startCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            //You have camera permission, so time to take a picture!
            startCameraForResult()
        }
        else {
            //No camera permission, so you need to ask for it first.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                parentView.brieflyShowSnackbar("You really need to give permission to use the camera.")
            }

            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCameraForResult()
            }
        }
    }

    private fun startCameraForResult() {
        startActivityForResult(Intent(this, CameraActivity::class.java), CAMERA_REQUEST_CODE)
    }

    private fun user(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private fun userName() = user()?.displayName ?: "Unknown"

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            displayChatMessages()
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getByteArrayExtra("picture")?.also {
                    sendMessage(Message(it.encode(), user = userName(), type = MessageType.IMAGE))
                }
            }
        }
    }

    private fun sendMessage(message: Message) = query
            .ref
            .push()
            .setValue(message)

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

    private fun newAdapter(): RecyclerView.Adapter<MessageHolder> {
        val options = FirebaseRecyclerOptions.Builder<Message>()
                .setLifecycleOwner(this)
                .setQuery(query, Message::class.java)
                .build()

        return MessageAdapter(options).apply {
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