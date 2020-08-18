package com.dylanc.retrofit.helper.annotations

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS)
annotation class ApiUrl(val value: String)