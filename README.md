# Step 3
Welcome to the third step of this tutorial! In this step you're going to learn about `RecyclerView`s and how to couple them to a `FirebaseDatabase` so that you can display messages to your users. You're also going to learn how to push new (for now just text) messages to the database.

### Goal of step 3
On completion of this step you can view messages other users have pushed to the database, and you can also send simple text messages yourself.

### Relevant classes
* `ChatActivity.kt`
* `MessageHolder.kt`
* `MessageAdapter.kt`

## RecyclerView
A quintessential element in Android is called a [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview.html). They're ideal for displaying large datasets in a scrollable list. As the name suggests, this view "recycles" it's elements (also called  as the appear and disappear from your actual view. A common mistake people make when just starting Android development, is putting logic in the `RecyclerView` element itself. **Remember, your RecyclerView is the UI element so it should be fairly "dumb"**. The logic for displaying your items should be handled by an [Adapter](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html), and since we're doing fancy stuff with a `FirebaseDatabase` we're using a `FirebaseRecyclerAdapter` in conjuction with `FirebaseRecyclerOptions`. Information about how to implement the most basic example using these classes can be found [here](https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md). This might (understandably) be a bit overwhelming, so to help speed things along `MessageHolder.kt` and `MessageAdapter.kt` are properly define. I encourage you to look through them to gain some understanding about them. Remember, copy-pasting code is fine (we've all been there...), but only if you understand what it does. :)

### A small summary
A `RecyclerView` is an Android UI element which governs a large amount of data. Each element of that data is displayed via a `RecyclerView.ViewHolder` and the creation and binding of those viewholders is governed by a `RecyclerView.Adapter`

## FirebaseDatabase
For this step we want to send and retrieve messages. This section will give you some pointers how to do this in an easy way.

### Sending messages
Since `query` already points to the correct section of our database, it's easiest to re-use that part for sending messages. From `query` you can get your `DatabaseReference` back, then specify that you want to **push** *-hint hint-* a **value** *-hint hint-* to your database. Optionally you can attach an `OnCompletionListener` on the method you use to set a value in which you can log a debug statement.

### Receiving messages


