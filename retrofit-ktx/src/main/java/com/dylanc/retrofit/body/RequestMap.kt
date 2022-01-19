@file:Suppress("unused")

package com.dylanc.retrofit.body

typealias RequestMap = @JvmSuppressWildcards Map<String, Any?>

fun requestMapOf(vararg pairs: Pair<String, Any?>) = mutableMapOf(*pairs)