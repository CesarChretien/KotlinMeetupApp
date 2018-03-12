## Step 4
Welcome to the fourth step in this tutorial! In this step we're shifting our focus back to something typically Android, which are intents, starting a new Activity and getting some information back from that Activity when it's finished.

### Goal of step 4
You'll have completed this step if you're able to launch a new Activity, enter some text and successfully send it back to your previous activity.

### Relevant classes
* `ChatActivity.kt`
* `CameraActivity.kt`

## Miscellaneous changes
`ChatActivity.kt` has changed a bit since last time. `editText` now has a `TextWatcher` attached to it, 

## Activities
It seems a bit late now to specifically touch upon one of the key classes of Android, but if your app only has one activity they're not super interesting (other than of course they're your main class, provide an entry point for your app, handle [lifecycle states](https://developer.android.com/guide/components/activities/activity-lifecycle.html) and much more. It gets way more interesting once multiple activities are involved. In this case we want to [launch an Activity and get a result back from that it](https://developer.android.com/training/basics/intents/result.html#StartActivity).

### Intents
As the name suggests, Intents are object which are used to signify you want to do something. You can view Intents as a courier with information and data to ferry inbetween Activities. They're used for starting a different Acitivity from one Activity, but also to send a result back when one activity finish.

## Kotlin Koolstuff
This installment of Kotlin Koolstuff is going to be significantly smaller than previous ones, but there's always something new to talk about

### Calling methods on nullable parameters
Remember that in Kotlin if you mark a type with `?` it is nullable. If an object is marked with such a type you can't call methods on it as normal, so for example:
```kotlin
var x: Thing? = ...
val xInfo = x.toString() //doesn't compile...
```
Will not work. How you should call methods on `x` goes as follows:
```kotlin
val xInfo = x?.toString() //compiles!
```
So what is happening here? `x?` denotes that `x` *might* be null. If this is the case, the entire statement will be null. The compiler knows that in the end you want `xInfo` to be of type `String`, but since it might also be null, the actual type of `xInfo` will be `String?`. But what if you really, really need `xInfo` to be a `String`. This can be done as follows: 
```kotlin
val xInfo = x?.toString() ?: "x can't be represented as a String"
```
`?:` is called the **elvis operator** (tilt your head 90 degrees to the left), which is saying "if somehow the statement to my left becomes null, assign what is to my right side to `xInfo`. This makes it so that `xInfo` is of non-null type `String`.
