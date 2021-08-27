package com.dylanc.retrofit.annotations

/**
 * @author Dylan Cai
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class DomainUrl(val name: String)