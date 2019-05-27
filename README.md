
# Mercari  App Code Setup

## Android Studio IDE
Version 3.4.1 of Android Studio IDE was used for developing the Mercari App 

##  Project Configurations

 - Gradle Version: 5.1.1
 - Gradle Build Tool Version: 3.4.1
 - Koltin Version: 1.3.31
 - Build Tool Version: 28.0.3
 - Minimum SDK Version:  [API 21](https://developer.android.com/reference/android/os/Build.VERSION_CODES.html#LOLLIPOP)
 
##  Android MVVM Base Architecture
This application is build using [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) architecture having Repository Design Pattern for data source. Following are key highlights of the project.
 - MVVM Architectural pattern
 - Offline Support using [Realm](https://realm.io/products/realm-database/)
 - Dependency Injection using [Dagger 2](https://github.com/google/dagger) with Dagger-android
 - [Android Jetpack](https://developer.android.com/jetpack/) libraries
	 - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
	 - [Paging Library](https://developer.android.com/topic/libraries/architecture/paging)
	 - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
	 - [Data Binding Library](https://developer.android.com/topic/libraries/data-binding)
 - [Retrofit 2](https://square.github.io/retrofit/) for network layer
 - [Glide](https://github.com/bumptech/glide) for Image loading
 - [Timber](https://github.com/JakeWharton/timber) for Logging
 - [Realm Monarchy](https://github.com/Zhuinden/realm-monarchy) A wrapper over Realm, that exposes RealmResults as various forms of LiveData.
 - Fabric.io Crashlytics for crash reporting tool.

The application has been built with  **offline support**. It has been designed using  **Android Architecture components**  with  **Realm**  for offline data caching. The application is built in such a way that whenvever there is a service call, the result will be stored in local database. If internet is not available application will display data from offline storage. 


## Application Architecture

<img src="/screenshots/screenshot_mvvm_diagram_overview.jpg" alt="MVVM"/>

The main advantage of using MVVM, there is no two-way dependency between ViewModel and Model, unlike MVP. Here the viewer can observe the data changes in the ViewModel as we are using LiveData which is lifecycle aware. The ViewModel to view communication is achieved through observer pattern basically observing the state changes of the data in the ViewModel. 

### Presentation Layer
The View layer is much more flexibile as an indefinite number of Views can bind to a ViewModel. Also, MVVM enforces a clear separation between Views and their master - ViewModel, as the latter holds no reference to Views. The model layer is completely isolated and centralized through the repository pattern.

### Model Layer

The model layer is structured on repository pattern so that the ViewModel has no clue on the origins of the data.

The repository handles data interactions and transactions from two main data sources - local and remote which is being handled by `NetworkBoundResource` and `NetworkPaginatedBoundResource` for pagination payload data.

There are two main use-cases, online and offline. In both use cases,  `NetworkBoundResource` or  `NetworkPaginatedBoundResource` will always send a call to the server for fetching latest data and cache it in local database and data is displayed to the user. In case of no internet connection,  data is always queried from local storage.

### Dependency Injection
Dagger2 is used to externalize the creation of dependencies from the classes that use them. Android specific helpers are provided by `Dagger-Android` and the most significant advantage is that they generate a subcomponent for each `Activity` through a new code generator.

<img src="/screenshots/screenshots_dagger_overview.png" alt="Dagger"/>

###  Strong points
-   Decoupling level is high.
-   Data Flow is centralized through Observable.
-   Possess high flexibility to create variants for automated and manual testing.
-   Possess lightweight structure due to MVVM presentation pattern.
-   Is scalable and easy to expand.

### Weak points
-   Possess high code base - simpler approaches will certainly lower code size

## Different Type Of Environments

There are four [build variant](https://developer.android.com/studio/build/build-variants.html) configured for the App, As in real application we have many environments due to this I create following build variant to support various environments as a base.

 - **Prod Build Variant:**
     -  This build variant contains both (Debug and Release mode)
     -  It contains a separate code base for web service URL.

 - **UAT Build Variant:**
     - This build variant contains both (Debug and Release mode)
     - It contains a separate code base for web service URL
   
 - **QA Build Variant:**
     - This build variant contains both (Debug and Release mode)
     -  It contains a separate code base for web service URL.

 - **Dev Build Variant:**
     - This build variant contains both (Debug and Release mode)
     - It contains a separate code base for web service URL.

<img src="/screenshots/screenshot_build_variant_support.png" alt="Build Variant"/>


## TODO Improvements Task

- [ ] Move to Navigation component Library after this [issues](https://issuetracker.google.com/issues/80029773#comment25) is fixed
- [ ] Improved error screen, currently I am just showing text.
- [ ] Pull to Refresh for Display Items. We can do this once we add Pagination API.
