package com.dylanc.retrofit.helper.annotations

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.CLASS)
annotation class ApiUrl(val value: String)