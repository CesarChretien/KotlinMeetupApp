package com.example.cesarchretien.kotlinmeetupapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

const val SIGN_IN_REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView.layoutManager = LinearLayoutManager(this.baseContext)

        if (FirebaseAuth.getInstance().currentUser == null) {
            //Start sign up/sign in activity
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(), SIGN_IN_REQUEST_CODE)
        }
        else {
            //User is already signed in.
            parent_view.brieflyShowSnackbar("Welcome ${FirebaseAuth.getInstance().currentUser?.displayName ?: "user"}")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SIGN_IN_REQUEST_CODE) {

            val resultIsOk = requestCode == Activity.RESULT_OK
            val snackbarMessage = if (resultIsOk) "Successfully signed in. Welcome!" else "We couldn't sign you in. Please try again later."

            parent_view.brieflyShowSnackbar(snackbarMessage)

            if (resultIsOk) {
                displayChatMessages()
            }
        }
    }

    private fun displayChatMessages() {

    }

    private fun View.brieflyShowSnackbar(message: String) = Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()

}