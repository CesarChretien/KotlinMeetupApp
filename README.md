# Step 2

Welcome to the second step of this tutorial, time to dive into actual code this time (welcome to Android development, where getting a basic app to run is half the battle)! Since we're building a chat app, authentication is crucial, which we're going to tackle during this step. 

## Goal of step 2

On completion of step 2, when you run the app, you should see a screen with a single line of text in the centre which reads "Currently logged in as: `<your name>`", where `<your name>` is the display name you chose upon anonymous sign in, or the name that's registered in your Google account (if you use that one to sign in).

## Firebase

[Firebase](https://firebase.google.com/) is a very helpful service provided by Google to easily implement all sorts of functionality into your app. Analytics, monetizing, nowadays even crash reports, the list goes on. In case of our app we're interested in the Authentication and Database part. Since this step is about Authentication we shall dive into that first.

## Signing in
[For this we're going to use the class AuthUI](https://firebase.google.com/docs/auth/android/firebaseui#signin). Usually a sign-in flow requires alot of thought, for example "When a user launches the app, I should check if he/she is signed in our not" and "When a user tries to create an account, we should check if an account with that name is already registered". Long story short: If we tried to implement a sign-in flow from scratch ourselves, we'd be out of time before we're even halfway done.

## Signing out

[On the same page which explains signing in](https://firebase.google.com/docs/auth/android/firebaseui#signout), signing out is also explained. Because the app does absolutely nothing when signed out, we want to close the app after signing out is complete.

## Relevant files for this step
* `ChatActivity.kt`
* `activity_main.xml`

## Kotlin Koolstuff

Welcome to the first Kotlin Koolstuff section! Each step will have one of these sections which gives you some information about Kotlin itself which might help you to implement 

### Methods and nullability 
```kotlin
fun method() {
  //code
}
```
By default, the return type of a method is `Unit`, which is the equivalent of Java's `void`. If your method has a return type other than that, your method signature changes into:
```kotlin
fun method(): SomeObject {
  //code
  return something
}
```
If your method signature is fairly simple, this is also valid syntax:
```kotlin
fun method() = something
```
Notice how `SomeObject` was omitted here? This called "type inference", which is an expensive way of saying that if your **compiler** has enough information about the return type of your method you don't need to specify it explicitly. Although when working in on a large project, specifying the return type might be useful for documentation reasons.
One final note: In contrast to Java, Kotlin treats nullability as a first class citizen. That is to say that you have to explicitly specify your types as nullable or not. If the return type of a method is `SomeObject`, that means it can never be null. But if you specify the return type as `SomeObject?`, then it might return an instance of `SomeObject` or `null`. Meaning putting a `?` behind your return type marks it as nullable.

### Functions as input parameters.

Suppose you have the following Java interface: 
```java
public interface SomeJavaInterface {
  void doSomething();
}
```
And suppose you have the following method signature in Java:
```java
public void javaMethod(@NonNull final SomeJavaInterface someJavaInterface) {
  //some code...
  someJavaInterface.doSomething();
  //some more code...
}
```
In the dark ages of pre-Java 8, making use of `javaMethod` looked something like this:
```java
javaMethod(new SomeInterface() {
  @Override
  public void doSomething() {
    //specify behaviour of doSomething() here.
  }
});
```
Yikes, that looks like an ocean of boilerplate code. Luckily, Java 8 decided to enter the 21st century and made it possible to use it like this:
```java
javaMethod(() -> { /* specify behaviour of doSomething() here. */ });
```
Much better! This is possible because `SomeInterface` is a functional interface, which means it has *exactly* one abstract method. 

Now how would one go about achieving this in Kotlin? Fairly simple actually. The behaviour of functional interfaces is baked into the language as functions. Functions can be stored in values as usual:
```kotlin
val f: () -> Unit = { /* do something that returns nothing here. */ }
```
Let's break this down: The signature `() -> Unit` means that `f` is a function which takes no input parameters (denoted by `()` and being on the left side of `->`) and doesn't return anything (denoted by `Unit` being on the right side of `->`.
Now let's try something fancier:
```kotlin
val g: (Int, Int) -> String = { x, y -> "$x + $y = ${x + y}" }
```
So what does this do exactly? `(Int, Int)` means `g` takes two integers as input parameters and `-> String` means that you get a `String` as output. So calling `println(g(5, 6))` will print `"5 + 6 = 11"`.

Alright, back to reproducing our Java equivalent in Kotlin:
```kotlin
fun kotlinMethod(doSomething: () -> Unit) {
  //some code...
  doSomething()
  //some more code...
}
```
...and that's it! No need to define a seperate interface first. To call this method it will look something like this:
```kotlin
kotlinMethod({ /* do something that returns nothing here. */ })
```
A small note: In Kotlin if the final input parameter of a method is a function, you can omit the parenthesis. Thus this code block is equivalent to the previous one:
```kotlin
kotlinMethod { /* do something that returns nothing here. */ }
```

So what's the grand idea behind these explanations? Java and Kotlin are 100% interoperable, and in this case you can treat a java method with a functional interface as an input parameter as if it's a function when you call it in Kotlin.

## Kotlinx synthetics
One of the huge benefits Kotlin has brought to Android is how to bind view elements from a `.xml` file into your Views and Activities. Suppose you have the following EditText (an element which allows for text input from a user) in your xml:
```xml
<EditText
    android:id="@+id/some_edit_text"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:hint="Enter some text here!" />
```
The oldest Java way to bind this in your code was as follows:
```java
final EditText someEditText = (EditText) findViewById(R.id.some_edit_text);
```
Luckily, nowadays `findViewById` accepts a generic type parameter so you don't have to explicitly cast every element anymore, and at some point the marvelous library called [ButterKnife](http://jakewharton.github.io/butterknife/) was introduced which meant you could bind your view elements to class variables like so:
```java
@BindView(R.id.some_edit_text)
EditText someEditText;
```
Which cleaned up alot of unneccessary code. Though Kotlin introduced something even better, which are [Kotlin Android Extensions](https://kotlinlang.org/docs/tutorials/android-plugin.html). This allows us in Kotlin code to simply call
```kotlin
some_edit_text
```
As an `EditText` element on which you can perform the neccessary operations to achieve whatever functionality you had in mind. If you decide to use this in your project you might want to consider changing the naming convention of your ids from `lowercase_separated_by_underscores` to `lowerCamelCase`.
