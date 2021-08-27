package com.dylanc.retrofit.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ApiUrl(val value: String)