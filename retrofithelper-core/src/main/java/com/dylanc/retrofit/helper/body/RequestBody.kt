@file:Suppress("unused", "NOTHING_TO_INLINE")

package com.dylanc.retrofit.helper.body

/**
 * @author Dylan Cai
 */

typealias RequestMap = @JvmSuppressWildcards Map<String, Any?>

inline fun requestMapOf(vararg pairs: Pair<String, Any?>) = mutableMapOf(*pairs)