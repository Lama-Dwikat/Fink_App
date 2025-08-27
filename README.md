This is the code for Android Mobile Application for Fink Website 
to run this code you have  to install Android Studio ,then clone this reposotiry
the app build using Kotlin and jetpack compose , for the rest api Retrofit library is used 
The application 
 These implementations were  added in the "build,gradle.kts(Module:app) to make the project work 

 These are for retrofit :
 implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.1")
    ----------------------------------------------------------------------------------------------------------------------------------------
    Thse are for colllectAsState() :
    implementation("androidx.compose.runtime:runtime:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    ----------------------------------------------------------------------------------------------------------------------------------------
    This is for the page navigation: 
    implementation("androidx.navigation:navigation-compose:2.8.0")
    ----------------------------------------------------------------------------------------------------------------------------------------
    for Aladin Lite skymap
    implementation ("androidx.webkit:webkit:1.8.0")
    ----------------------------------------------------------------------------------------------------------------------------------------
    For Page Navigation
    implementation("androidx.navigation:navigation-compose:2.8.0")
    ----------------------------------------------------------------------------------------------------------------------------------------

After run this application you will have the main page which is a search bar you can search by objectId, conesearch and class name , you can see the results in two different vies ,
and if you click on objectId in the result view you will have another page that contain all details of that object

Simply clone the repository, run the code, and enjoy exploring the app.
