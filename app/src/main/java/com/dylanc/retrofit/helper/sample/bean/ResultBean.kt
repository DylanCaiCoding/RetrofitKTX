package com.dylanc.retrofit.helper.sample.bean

/**
 * @author Dylan Cai
 * @since 2019/9/28
 */
data class ResultBean<T>(
  val code: Int,
  val msg: String,
  val data: T
)