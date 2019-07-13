package com.dylanc.retrofit.helper

import com.dylanc.retrofit.helper.transformer.DownloadTransformer
import io.reactivex.Observable
import me.jessyan.progressmanager.ProgressListener
import java.io.File

/**
 * @author Dylan Cai
 * @since 2019/7/13
 */
class DownloadTask private constructor(private val builder: Builder) {

  companion object {
    @JvmStatic
    fun with(url: String): Builder {
      return Builder(url)
    }
  }

  init {
    builder.progressListener?.let { RetrofitManager.addDownloadListener(builder.url, it) }
  }

  fun download(): Observable<File>? {
    return RetrofitHelper.create(DownloadService::class)
      .download(builder.url)
      .compose(DownloadTransformer.handle(builder.pathname))
  }

  class Builder internal constructor(internal val url: String) {
    internal lateinit var pathname: String
    internal var progressListener: ProgressListener? = null

    fun progressListener(progressListener: ProgressListener): Builder {
      this.progressListener = progressListener
      return this
    }

    fun downloadTo(pathname: String): Observable<File> {
      this.pathname = pathname
      return build().download()!!
    }

    private fun build() = DownloadTask(this)
  }
}