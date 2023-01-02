<br />
<p align="center">
  <h2 align="center">Learning Numbers</h2>
  <p align="center">
  A guide to help kids learn numbers!

<p align="center">
  <img src="https://badges.frapsoft.com/os/v3/open-source.svg?v=103">
<br>
  <img src="https://img.shields.io/badge/Project-2022-1f425f.svg?color=red">
  <img src="https://img.shields.io/badge/HMS%20-MLKit-1f425f.svg?color=orang">
  <img src="https://img.shields.io/badge/MLKit-ASR-1f425f.svg">
  <img src="https://img.shields.io/badge/MLKit-ObjectDetection-1f425f.svg">
<br>
 <img src="https://img.shields.io/badge/language-kotlin-blue">
  <img src="https://img.shields.io/badge/minSDK-22-orange">
  <img src="https://img.shields.io/badge/androidGradleVersion-7.1.0-green">
  <img src="https://img.shields.io/badge/gradleVersion-7.4-informational">
</p>
    

<hr>
                   

<p float="left">
  <img src="https://github.com/Explore-In-HMS/LearningNumbers/blob/main/Images/LN1.jpg?raw=true" width="500"  />


  <img src="https://github.com/Explore-In-HMS/LearningNumbers/blob/main/Images/LN2.gif" width="500" />
</p>

</p>

</table>
                                                                                                     



# Introduction


Learning Numbers app is designed to help kids learn basic counting and arithmetic skills in a fun and interactive way by Huawei ML Kit. Using the latest technology, our app allows kids to generate a random image or choose one from their own gallery. Then, using Huawei Object Detection, the app can detect the number of items in the image.

Next, it's up to the kids to count the items and tell us how many there are using their microphone. Our Huawei ASR (automatic speech recognition) feature will then listen to their answer and give us the result as well. We compare the result from Huawei object detection with the answer given by the child to see how well they did.

Not only is this app a fun and engaging way for kids to learn about counting, but it also best guide for check results



# About Huawei ML Kit

ML Kit allows your apps to easily leverage Huawei's long-term proven expertise in machine learning to support diverse artificial intelligence (AI) applications throughout a wide range of industries. Thanks to Huawei's technology accumulation, ML Kit provides diversified leading machine learning capabilities that are easy to use, helping you develop various AI apps. [More..](https://developer.huawei.com/consumer/en/hms/huawei-mlkit/)

## Automatic Speech Recognition
Automatic speech recognition (ASR) can recognize speech not longer than 60s and convert the input speech into text in real time. This service uses industry-leading deep learning technologies to achieve a recognition accuracy of over 95%.
## Object Detection
The object detection and tracking service can detect and track multiple objects in an image, so they can be located and classified in real time. A maximum of eight objects can be detected and tracked concurrently



# What You Will Need

## Development Enviroment

-	JDK version: 1.8.211 or later
- Android Studio version: 3.X or later
-	minSdkVersion: 19 or later (mandatory)
-	targetSdkVersion: 30 (recommended)
-	compileSdkVersion: 30 (recommended)
-	Gradle version: 4.6 or later (recommended)

<p>

- Test device: a Huawei phone running EMUI 5.0 or later, or a non-Huawei phone running
- HMS Core (APK) 5.0.0.300 or later
  
## Getting Started

-	Register a developer account on HUAWEI Developers and configure.
- Register in to [Huawei Developer Console](https://developer.huawei.com/consumer/en/console) and Create and configure an app
-	To use ML  Kit, you need to enable it in AppGallery Connect. For details, please refer to [Enabling Services](https://developer.huawei.com/consumer/en/doc/distribution/app/agc-help-enabling-service-0000001146598793).
- Adding the AppGallery Connect Configuration File of Your App
    	- Sign in to AppGallery Connect and click My projects.
      - Find your project and click the app for which you want to integrate the HMS Core SDK.
      - On the Project Setting page, set SHA-256 certificate fingerprint to the SHA-256 fingerprint you've generated. 
      - Go to Project settings > General information. In the App information area, download the agconnect-services.json file.
-	Configuring the Maven Repository Address for the HMS Core SDK
-	Open the build.gradle file in the root directory of your Android Studio project.
-	Add the AppGallery Connect plugin and the Maven repository.


```sh
buildscript { 
    repositories { 
        google() 
        jcenter() 
        // Configure the Maven repository address for the HMS Core SDK. 
        maven {url 'https://developer.huawei.com/repo/'} 
    } 
    dependencies { 
        ... 
        // Add the AppGallery Connect plugin configuration. You are advised to use the latest plugin version. 
        classpath 'com.huawei.agconnect:agcp:1.7.1.300' 
    } 
} 
 
allprojects { 
    repositories { 
        google() 
        jcenter() 
        // Configure the Maven repository address for the HMS Core SDK. 
        maven {url 'https://developer.huawei.com/repo/'} 
    } 
} 

```


Adding Build Dependencies (app build gradle)
```sh

    implementation "com.huawei.agconnect:agconnect-core:$1.7.1.300"
    // Import the base SDK.
    implementation 'com.huawei.hms:ml-computer-vision-object:3.7.0.301'
    // Import the object detection and tracking model package.
    implementation 'com.huawei.hms:ml-computer-vision-object-detection-model:3.7.0.301'
    // Speech to text
    implementation "com.huawei.hms:ml-computer-voice-asr-plugin:3.7.0.301"

```

Permissions ( AndroidManifest.xml )

```sh
    <!--use mic-->
    <uses-permission android:name="android.permission.RECORD_AUDIO"/>
    <!--get image from gallery-->
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
    <!--for services-->
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE"/>
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE"/>


```


# References

The resources used in the development of the project are as follows:

* [HMS ML Kit](https://developer.huawei.com/consumer/en/hms/huawei-mlkit/)
* [HMS ML-Object Detection](https://developer.huawei.com/consumer/en/doc/development/hiai-Guides/object-detection-track-0000001050038150)
* [HMS ML-ASR](https://developer.huawei.com/consumer/en/doc/development/hiai-Guides/ml-asr-0000001050066212)


[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![GitHub visitors](https://img.shields.io/badge/dynamic/json?color=red&label=visitors&query=value&url=https%3A%2F%2Fapi.countapi.xyz%2Fhit%2FExplore-In-HMS.LearningNumberss%2Freadme)<br>
Copyright © 2022 <br />
