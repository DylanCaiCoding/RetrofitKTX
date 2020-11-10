package com.dylanc.retrofit.helper.annotations

/**
 * @author Dylan Cai
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class Domain(val value: String)