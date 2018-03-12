package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.MenuRes
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
                sendMessage { user -> Message(messageText, user) }
                editText.clear()
            }
            else {
                startCamera()
            }
        }
    }

    private fun startCamera() {
        TODO("""
            Here, you want to start your camera, but since Android 6 and above, permissions are asked during run time,
            not on install. So here you should check if a user has already granted use of the device's camera. If yes, start the camera.
            If not, go into the request camera permission flow.
            """)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        TODO("""
            After exiting the camera permission flow, this method will be called with the result of that flow.
            If the user has granted camera access, we should start the camera immediately.
            """)
    }

    private fun startCameraForResult() {
        startActivityForResult(Intent(this, CameraActivity::class.java), CAMERA_REQUEST_CODE)
    }

    private fun user(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            displayChatMessages()
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                data?.getByteArrayExtra("picture")?.also {
                    sendMessage { user -> Message(it.encode(), user = user, type = MessageType.IMAGE) }
                }
            }
        }
    }

    private fun sendMessage(action: (user: String) -> Message) = query.ref.push().setValue(action(user()?.displayName
            ?: "Unknown"))

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