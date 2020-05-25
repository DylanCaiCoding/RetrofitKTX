package com.dylanc.retrofit.helper.annotation

@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class Domain(val value: String)