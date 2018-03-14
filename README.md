# Step 3
Welcome to the third step of this tutorial! In this step you're going to learn about `RecyclerView`s and how to couple them to a `FirebaseDatabase` so that you can display messages to your users. You're also going to learn how to push new (for now just text) messages to the database.
To start this step, enter the following in your terminal:

`git checkout step-3 -f`


### Goal of step 3
On completion of this step you can view messages other users have pushed to the database, and you can also send simple text messages yourself.

### Relevant classes
* `ChatActivity.kt`
* `MessageHolder.kt`
* `MessageAdapter.kt`

## RecyclerView
A quintessential element in Android is called a [RecyclerView](https://developer.android.com/guide/topics/ui/layout/recyclerview.html). They're ideal for displaying large datasets in a scrollable list. As the name suggests, this view "recycles" it's elements (also called  as the appear and disappear from your actual view. A common mistake people make when just starting Android development, is putting logic in the `RecyclerView` element itself. **Remember, your RecyclerView is the UI element so it should be fairly "dumb"**. The logic for displaying your items should be handled by an [Adapter](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.Adapter.html), and since we're doing fancy stuff with a `FirebaseDatabase` we're using a `FirebaseRecyclerAdapter` in conjuction with `FirebaseRecyclerOptions`. Information about how to implement the most basic example using these classes can be found [here](https://github.com/firebase/FirebaseUI-Android/blob/master/database/README.md). This might (understandably) be a bit overwhelming, so to help speed things along, MessageHolder.kt and MessageAdapter.kt are already properly defined. I encourage you to look through them to gain some understanding about them. Remember, copy-pasting code is fine (we've all been there...), but only if you understand what it does. :)

### A small summary
A `RecyclerView` is an Android UI element which governs a large amount of data. Each element of that data is displayed via a `RecyclerView.ViewHolder` and the creation and binding of those viewholders is governed by a `RecyclerView.Adapter`

## FirebaseDatabase
For this step we want to send and retrieve messages. This section will give you some pointers how to do this in an easy way.

### Sending messages
Since `query` already points to the correct section of our database, it's easiest to re-use that part for sending messages. From `query` you can get your `DatabaseReference` back, then specify that you want to **push** *-hint hint-* a **value** *-hint hint-* to your database. Optionally you can attach an `OnCompletionListener` on the method you use to set a value in which you can log a debug statement.

### Receiving messages
As soon as someone sends a new message to the database you need to make sure this message displayed to all your other users. Since we're basically changing the content of our UI this means this should be handled by the adapter attached to your `RecyclerView`. A `FirebaseRecyclerAdapter` accepts a `FirebaseRecyclerOptions` as argument, which has been conveniently provided for you. :)

## Kotlin Koolstuff
Welcome to another installment of Kotlin Koolstuff! Again, some basics about Kotlin can be found here which might help you to implement the features described in this step.

### Creating objects
Instantiating an object in Kotlin is fairly simply. Creating an object an storing it in a `val` goes as follows:
```kotlin
val x = Thing()
```
Here, `x`'s type is inferred by the compiler as `Thing`, so when making use of `x` it will be recognized as such.
Now let's assume you have a `Person` object with a name and an age. In Kotlin you'd declare something like that as:
```kotlin
data class Person(var name: String, var age: Int)
```
And that's it. Notice the `data` modifier in front of this class? This means that you get the `equals()`, `hashCode()`, `copy()`, `toString()`, setter and getter methods for free! Both variables are marked as `var`, which means they're mutable. If you mark a variable as `val`, it's immutable and after invoking the constructor you can't change it.
Making sure an object is well-defined for FirebaseDatabase, it needs a **default constructor**, meaning that we can instantiate it without any parameters. How would you tackle this? The naive way would be to redefine a constructor that takes no input parameters for your class. But we can do better, in Kotlin we can make use of **default arguments** like so:
```kotlin
data class Person(var name: String = "Anonymous", var age: Int = -1)
```
Meaning `println(Person())` will print `Person(name=Anonymous, age=-1)`. But what if I want to remain "Anonymous" as a person, but I do want to supply a proper age? Well, Kotlin's got you covered with **named arguments**, this allows you to instantiate a Person as follows:
```kotlin
val p = Person(age = 42)
println(p)
```
Which will print `Person(name=Anonymous, age=42)`.

### Using setters and getters between Java and Kotlin
Suppose thing looks as follows in Java:
```java
public class Thing {  
  private String value;
  
  public String getValue() {
    return value;
  }
  
  public void setValue(final String value) {
    this.value = value;
  }
}
```
Properly formatted POJOs like this can be accessed in Kotlin as follows:
```kotlin
val t = Thing()
println(t.value) //uses the getter. prints whatever value has.
t.value = "new value" //uses the setter. sets value to whatever is on the right of =.
```

### Anonymous classes
We've already seen the case where in Java you have a functional interface and you can treat it as a function. But what if we're dealing with an abstract class or interface which has more than one abstract method? In that case we really have to create an anonymous class defining those behaviours. Suppose we have the following:
```java
public interface TwoMethodInterface {
  void method1();
  
  void method2();
}
```
I will forego the Java implementation, but it's basically the pre-Java 8 functional interface implementation but with multiple methods. Suppose you have a method `niceMethod(@NonNull final TwoMethodInterface twoMethodInterface)`. In Kotlin you can use it as:
```kotlin
niceMethod(object : TwoMethodInterface() {
  override fun method1() = /* do something here. */
  
  override fun method2() = /* do something else here. */
})
```
Pretty straightforward. Three small notes here, the first being that you instantiate abstract entities with `object : `, second is that `override` is a proper keyword in Kotlin and last but not least: In this example we're overriding two methods, but it's also possible to override just one method as long as all methods are properly defined in a superclass.

### Advanced function definitions
Ever been in the situation where you had to do this?:
```java
SomeObject someObject = new SomeObject();
someObject.doThis();
someObject.doThat();
//etc..
```
With Kotlin we can clean this up a little with the `apply` function which is contained in the [Kotlin Standard Library](https://kotlinlang.org/api/latest/jvm/stdlib/index.html) and is roughly defined as:
```kotlin
inline fun <T> T.apply(block: T.() -> Unit): T {
  block()
  return this
}
```
Looks simple enough, but secretly *alot* is happening here:
* `inline` is there because this method accepts a function as input-parameter, which causes `block` not to be translated to an anonymous inner class, which potentially can cause memory leaks and performance overhead.
* `<T>` after `fun` denotes a generic parameter.
* `T.apply` means this is an **extension function** of `T`. `T` here can even be a normally unmodifiable class (think `String` for example)
* `block: T.() -> Unit` means that block is a function with as **receiver type** `T`. This is a fancy way of saying `block` is treated as a function which belongs to `T`.
These will take a bit to wrap your head around, but are a very powerful feature of Kotlin. The Java code block describing the usage of `someObject` can be simplified as follows:
```kotlin
val someObject = SomeObject().apply {
  doThis()
  doThat()
}
```
If you're interested, [this](https://proandroiddev.com/the-difference-between-kotlins-functions-let-apply-with-run-and-else-ca51a4c696b8) is a nice article about some of the functions contained in the Kotlin Standard library (and their subtle differences).

## Navigation
* Previous step: https://github.com/CesarChretien/KotlinMeetupApp/blob/step-2/README.md
* Next step: https://github.com/CesarChretien/KotlinMeetupApp/blob/step-4/README.md
