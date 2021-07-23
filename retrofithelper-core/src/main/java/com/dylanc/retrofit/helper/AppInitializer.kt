@file:Suppress("unused", "ObjectPropertyName")

package com.dylanc.retrofit.helper

import android.app.Application
import android.content.Context
import androidx.startup.Initializer

/**
 * @author Dylan Cai
 */

internal lateinit var application: Application private set

internal class AppInitializer : Initializer<Unit> {

  override fun create(context: Context) {
    application = context as Application
  }

  override fun dependencies() = mutableListOf<Class<Initializer<*>>>()
}