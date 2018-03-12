package com.example.cesarchretien.kotlinmeetupapp

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
                val editTextIsEmpty = s?.isEmpty() ?: true
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
        startCameraForResult()
    }

    private fun startCameraForResult() {
        TODO("""
            Alright, the name might be confusing, but the activity you're going to launch is going to be your camera preview in the future.
            For now though, it'll show you a simple EditText and a button. But anyways, here is where you should launch a new Activity for a result back.
            """)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        TODO("""
            As soon as your second activity closes, this method will get called. Make sure you understand what
            requestCode, resultCode and data entail. From here you should be able to get the message you entered in your second activity.
            """)
    }

    private fun user(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

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