package com.example.cesarchretien.kotlinmeetupapp

import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.v7.app.AppCompatActivity
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
const val MAXIMUM_MESSAGES = 50
const val DATABASE_NAME = "chats"

private const val TAG = "ChatActivity"

class ChatActivity : AppCompatActivity(), FirebaseAuth.AuthStateListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fab.setImageResource(R.drawable.ic_send_white_24dp)
        TODO("""
            Here you need to do two things:
            1. As soon as you click "fab", send the text that's in "editText" to our database (but only if there's some actual text to send)
            2. Recyclerviews need to explicitly have a LayoutManager attached, otherwise they show nothing...
            """)
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

    private fun Menu?.inflate(@MenuRes menuRes: Int): Boolean {
        menuInflater.inflate(menuRes, this)
        return true
    }
    /**
     *
     * This field returns a "Query" object which is needed to create an adapter for your RecyclerView,
     * it's already hooked into the correct database and fetches the latest {@link MAXIMUM_MESSAGES} messages.
     */
    private val query = FirebaseDatabase
            .getInstance()
            .reference
            .child(DATABASE_NAME)
            .limitToLast(MAXIMUM_MESSAGES)

    override fun onAuthStateChanged(firebaseAuth: FirebaseAuth) {
        if (firebaseAuth.currentUser != null) {
            TODO("""
                If the currentUser is not null, it means you're logged in, so you should attach
                an adapter to your recycler view so you can display messages from the database.
                """)
        }
    }

    private fun user(): FirebaseUser? = FirebaseAuth.getInstance().currentUser

    private fun sendMessage(messageText: String, messageType: MessageType) {
        TODO("""
            A pretty self-explanatory function: This handles sending messages to our database
            The messageText should contain whatever you just typed and messageType should be
            TEXT, for now.
            """)
    }

    private fun newAdapter(): RecyclerView.Adapter<MessageHolder> {
        val options = FirebaseRecyclerOptions.Builder<Message>()
                .setLifecycleOwner(this)
                .setQuery(query, Message::class.java)
                .build()

        TODO("""
            Here you need to do two things as well:
            1. Create your adapter which handles what your recycler view displays.
            2. Register a data observer which "listens" to database changes (in this case: someone pushes a new message)
               and causes your recycler view to scroll to the last item.
            """)
    }
}