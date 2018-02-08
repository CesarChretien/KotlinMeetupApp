package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.annotation.MenuRes
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_main.*

const val SIGN_IN_REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this)

        if (FirebaseAuth.getInstance().currentUser == null) {
            //Start sign up/sign in activity
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE)
        }
        else {
            //User is already signed in.
            parentView.brieflyShowSnackbar("Welcome ${FirebaseAuth.getInstance().currentUser?.displayName ?: "user"}")
        }

        fab.setOnClickListener { _ ->
            FirebaseDatabase.getInstance()
                    .reference
                    .push()
                    .setValue(ChatMessage(editText.text.toString(), FirebaseAuth.getInstance().currentUser?.displayName ?: "Unknown"))

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
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            parentView.brieflyShowSnackbar("You have been signed out.")
                        }
                    }
        }

        return true
    }

    private fun displayChatMessages() {

    }

    private fun View.brieflyShowSnackbar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()

    private fun Menu?.inflate(@MenuRes menuRes: Int): Boolean {
        menuInflater.inflate(menuRes, this)
        return true
    }

    private fun TextView.clear() {
        this.text = ""
    }
}