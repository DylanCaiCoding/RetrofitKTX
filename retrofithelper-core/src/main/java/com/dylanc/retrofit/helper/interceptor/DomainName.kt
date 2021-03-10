package com.dylanc.retrofit.helper.interceptor

/**
 * @author Dylan Cai
 */

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class DomainName(val value: String)
