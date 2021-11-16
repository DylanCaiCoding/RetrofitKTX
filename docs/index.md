[![](https://jitpack.io/v/DylanCaiCoding/RetrofitKTX.svg)](https://jitpack.io/#DylanCaiCoding/RetrofitKTX)  [![License](https://img.shields.io/badge/License-Apache--2.0-blue.svg)](https://github.com/DylanCaiCoding/RetrofitHelper/blob/master/LICENSE)

## Gradle

Add it in your root build.gradle at the end of repositories:

```groovy
allprojects {
    repositories {
        // ...
        maven { url 'https://www.jitpack.io' }
    }
}
```

Add dependencies：

```groovy
dependencies {
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-ktx:1.0.0-beta'
    kapt 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-compiler:1.0.0-beta'
    
    // The following are optional, please add as needed
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-coroutines:1.0.0-beta'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-rxjava2:1.0.0-beta'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-autodispose:1.0.0-beta'
}
```
