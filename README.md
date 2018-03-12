## Step 5
Almost there! Step 5 is going to explain another crucial part of Android development, which are runtime permissions.

### Goal of step 5
You'll have completed step 5 successfully if once gaining camera permission and you open the `CameraActivity`, you will see a `Snackbar` on the bottom on the screen with "You have camera permission."

### Relevant classes
* `ChatActivity.kt`
* `CameraActivity.kt`

## Runtime permissions
You can find everything you need to know about runtime permissions [here](https://developer.android.com/training/permissions/requesting.html). A small note about the method `shouldShowRequestPermissionRationale()` though. The method may look daunting but it's explained in the runtime permission explanation page but I feel it's important enough to explicitly state the explanation here: This method returns `true` if the user has previously denied this permission before but hasn't ticked the "don't show again" box in the request permission dialog.

### Which permission?
It might be incredibly obvious, but just so there can be no ambiguity about which permission to grant: We're going to ask for the **CAMERA** permission.

## Kotlin Koolstuff
As cool as Kotlin may be, we have nothing relevant for this section. But since we're using alot of `if-else` statements here I'd like to discuss a little bit of control flow in Kotlin. Again, consider the following Java method
```java
public String whatsThis(final int x) {
  String res;
  
  if (x > 5) {
    res = "x is greater than 5!";
  }
  else {
    res = "x is not greater than 5...";
  }
  
  return res;
}
```
In Kotlin, an `if-else` block can return a value, that is to say in Kotlin the equivalent code of this is
```kotlin
fun whatsThis(val x: Int) {
  val res = if (x > 5) {
    "x is greater than 5!"
  else {
    "x is not greater than 5..."
  }
  
  return res
}
```
If you feel like your statement in the `if-else` blocks are small enough, you can even omit the curly brackets.
