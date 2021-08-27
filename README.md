# RetrofitKTX

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
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-ktx:1.0.0-alpha'
    kapt 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-compiler:1.0.0-alpha'
    
    // The following are optional, please add as needed
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-coroutines:1.0.0-alpha'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-rxjava2:1.0.0-alpha'
    implementation 'com.github.DylanCaiCoding.RetrofitKTX:retrofit-autodispose:1.0.0-alpha'
}
```

## License

```
Copyright (C) 2019. Dylan Cai

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
