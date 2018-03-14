## The final step
Welcome to the final step! This step introduces actual camera functionality, but that's actually not what this step is about. This step is about explaining what, in my opinion, is the best thing that Kotlin brought to Android development.

### The best thing?
Yep. The Android [Camera API](https://developer.android.com/guide/topics/media/camera.html) is a really old API. In fact it's so old it has been deprecated for API 21 (Android 5.0) or newer in favor of the Camera2 API. But since the example by google describing and implementing the base functionality of the actual Camera2 element [is over 800 lines long](https://github.com/googlesamples/android-Camera2Basic/blob/master/kotlinApp/Application/src/main/java/com/example/android/camera2basic/Camera2BasicFragment.kt), I did not want to bother people following this track with that (or myself for that matter).
So now what if you're stuck using an old API? That's where Kotlin really shines. The ability to combine extension and **higher-order** functions (higher-order meaning a method that accepts functions as input parameters), makes it so you can easily extend old APIs with various convenience methods which you can keep using. The [KTX](https://github.com/android/android-ktx) open source project contains alot of these examples, so if you're interested check it out!

### Relevant classes
* `CameraActivity.kt`
* `CameraPreview.kt`

### Goal of the final step
You'll have completed this step if you're now finally able to send images to the chat application as well. Take some photo's!

## Kotlin Koolstuff
Welcome to the final installment of Kotlin Koolstuff. As mentioned before, one of the nicest things about Kotlin is being able to basically molding an old API to your own liking by extending it with your own methods. Take [creating a camera preview](https://developer.android.com/guide/topics/media/camera.html#camera-preview) for example. Notice how often a `try-catch` is being used? Wouldn't it be nice if we can define a function on `Camera` which allows you to execute any method from `Camera` and when it throws an exception it will be caught and logged? In Kotlin you can achieve this by applying the following two points:
1. Remember how an `if-else` block can be used in Kotlin? The same holds for a `try-catch` block.
2. You method signature will be that of a higher-order function.
That's it! If you want the whole shebang check out the `master` branch. I hope this was educational and you enjoyed this Android course. Comments and feedback are always welcome, and don't be shy to contact me if you have any questions. :)

## Navigation
* Previous step: https://github.com/CesarChretien/KotlinMeetupApp/tree/step-5
