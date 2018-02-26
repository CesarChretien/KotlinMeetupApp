package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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

class MainActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

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
            }
            else {
                startActivityForResult(Intent(this, CameraActivity::class.java), 100)
            }
        }
    }

    private fun user(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            displayChatMessages()
        }
        else if (requestCode == CAMERA_REQUEST_CODE) {
            parentView.brieflyShowSnackbar("Came back from camera.")
        }
    }

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