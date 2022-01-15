package com.dylanc.retrofit.sample.kotlin.bean

/**
 * @author Dylan Cai
 * @since 2019/9/28
 */
data class ApiResponse<T>(
  val code: Int,
  val msg: String,
  val data: T
)
