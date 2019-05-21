# Task: “Create a Mini Mercari App”

Given a specific hypothetical situation, implement an app imitating Mercari’s timeline.

## Hypothetical Situations

### A base for an app that will be developed and maintained for a long time

You are the leader of a five-person Android team. This team will be developing a new Android app and the project is expected to be developed and maintained by the team for a long time. As the leader, you will be designing the app and implementing the basics before the rest of the team begins work on it. Your output will be used as the base upon which more features will be added in the future.

## Minimum spec requirements for the app

- Call the screen shown in the example image (see below) the “Timeline”

![UI specifications](https://s3-ap-northeast-1.amazonaws.com/m-et/Android/images/39693654-9745-4d4d-8d03-25657d95c872.jpg)

- On startup, access https://s3-ap-northeast-1.amazonaws.com/m-et/Android/json/master.json and get product data for each category (one tab)
- Display the Timeline as a grid with two columns
- Display the number of `likes` and `comments` on the grid
- For `sold out` items, display a `sold out` label on the grid
- Display a floating button to list a new item in the position shown in the image below
- Screen rotation is required
- However, switching layouts when rotating the screen (in other words, creating a separate layout for landscape view) is not required
- Please create an app that is *production-level* quality (pretend this app will be downloaded from the Play Store and used by the general public)

## Requirements

- Programming language (your choice will not affect your evaluation)
    - Java or Kotlin
- The minimum SDK is 5.0 (Lollipop)
- You may use open source libraries if necessary
- All images necessary to implement the minimum requirements are in the res folder, so feel free to use them when necessary

## Submission

- A complete project that can be built in Android Studio and installed on a device immediately after being cloned from Github
- Make sure to include a Readme.md
    - If there is anything else regarding your submission you would like to highlight or explain, include that here
- Feel free to use CI tools and the like in order to optimize your development process (If you do so, you must include any configuration files, etc in your submission)
