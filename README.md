## Intro

This app has been made to introduce people to Kotlin and make something fancy in the process.

## Before we start

To be able to edit and run this app, you need [Android Studio 3.0](https://developer.android.com/studio/index.html) or higher, since that version comes with Kotlin included out of box.

After installing, choose **Check out project from Version Control** -> **Git** and put as **Git Repository URL**: https://github.com/CesarChretien/KotlinMeetupApp

Run the project and happy hacking!

# Step 1

## Introduction

Sadly, nothing fancy is going to happen here. This branch is the bare-bones project but with a all the dependencies in the `build.gradle` and `AndroidManifest.xml` files so you don't have to worry about those. Getting the setup of those files done correctly is an important part of Android development, but not something we want to both you with during this project. :) If you're interested you can look into them (and don't be shy to ask questions either)!

## Getting to run the app.

In Android Studio, press the green triangle located somewhere on top or press **CTRL** + **R**. You'll be prompted with a **Select Deployment Target** window where you can select on which device you want to run your app. You can choose to either run it on a physical device or a virtual device (also called an emulator). Go to "Run the app on your physical device" if you have an Android phone you can connect to your computer *and* you want to run the app on that phone, otherwise go to "Run the app on an emulator".

## Run the app on your physical device

[Read how to enable developer options on your phone first.](https://developer.android.com/studio/debug/dev-options.html) Afterwards you should be prompted by your phone to allow access by your computer. If everything has gone well you can now select your phone from the **Select Deployment Target** prompt and the app should start building and subsequently be installed on your phone.

## Run the app on an emulator

If you already have an emulator set up, it should appear under the "Available virtual devices". If not, press the "Create New Virtual Device" button in the lower left of your prompt. Select from the tab "Phone" a device with a "play" icon present in the column "Play Store" (your options probably are a Nexus 5 or Nexus 5X) and click "Next" in the bottom right. Secondly you have to pick a System Image of API level **21 or higher**. Under the "recommended" tab API level 24 and up is listed so picking either of them should be fine. At the time of writing the most recent stable version is API level 27, also known as 8.1 Oreo.

## Next step

On successfully building, installing and launching the app, you should see a screen with the text "Good job! Time to go to step 2!" in the centre. Which means it's time to do exactly that. :)
