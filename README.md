# Step 2

Welcome to the second step of this tutorial, time to dive into actual code this time (welcome to Android development, where getting a basic app to run is half the battle)! Since we're building a chat app, authentication is crucial, which we're going to tackle during this step. 

## Goal of step 2

On completion of step 2, when you run the app, you should see a screen with a single line of text in the centre which reads "Currently logged in as: `<your name>`", where `<your name>` is the display name you chose upon anonymous sign in, or the name that's registered in your Google account (if you use that one to sign in).

## Firebase

[Firebase](https://firebase.google.com/) is a very helpful service provided by google to easily implement all sorts of functionality into your app. Analytics, monetizing, nowadays even crash reports, the list goes on. In case of our app we're interested in the Authentication and Database part.
[For this we're going to use the class AuthUI](https://firebase.google.com/docs/auth/android/firebaseui#signin).
