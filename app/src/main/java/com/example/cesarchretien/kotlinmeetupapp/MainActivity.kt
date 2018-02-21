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
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

const val SIGN_IN_REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {

    private val query = FirebaseDatabase.getInstance().reference.limitToLast(50)
    private val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (user == null) {
            //Start sign up/sign in activity
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE)
        }
        else {
            //User is already signed in.
            parentView.brieflyShowSnackbar("Welcome ${user.displayName ?: "user"}")
            displayChatMessages()
        }

        fab.setOnClickListener {
            query.ref.push().setValue(ChatMessage(editText.text.toString(), user?.displayName ?: "Unknown"))
            editText.clear()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {

            val resultIsOk = resultCode == Activity.RESULT_OK
            val snackbarMessage = if (resultIsOk) "Successfully signed in. Welcome!" else "We couldn't sign you in. Please try again later."

            parentView.brieflyShowSnackbar(snackbarMessage)

            if (resultIsOk) {
                displayChatMessages()
            }
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