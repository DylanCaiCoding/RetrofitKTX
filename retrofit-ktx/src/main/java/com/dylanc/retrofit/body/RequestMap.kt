@file:Suppress("unused")

package com.dylanc.retrofit.body

/**
 * @author Dylan Cai
 */

typealias RequestMap = @JvmSuppressWildcards Map<String, Any?>

fun requestMapOf(vararg pairs: Pair<String, Any?>) = mutableMapOf(*pairs)