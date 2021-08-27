package com.dylanc.retrofit.sample.kotlin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dylanc.retrofit.sample.kotlin.databinding.ActivityMainBinding

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
    }
  }
}