package com.dylanc.retrofit.helper.sample.kotlin.ui

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dylanc.retrofit.helper.sample.kotlin.databinding.ActivityMainBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    val binding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(binding.root)
    with(binding) {
      btnCoroutines.setOnClickListener {
        startActivity(Intent(this@MainActivity, CoroutinesActivity::class.java))
      }
      btnRxjava.setOnClickListener {
        startActivity(Intent(this@MainActivity, RxJavaActivity::class.java))
      }
//      val uri:Uri = Uri.parse("")
//      val content = contentResolver.openInputStream(uri)!!.readBytes()
//      content.toRequestBody("multipart/form-data".toMediaTypeOrNull(), 0, content.size)
    }
  }
}