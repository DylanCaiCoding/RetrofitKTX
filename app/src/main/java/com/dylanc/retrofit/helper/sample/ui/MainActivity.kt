package com.dylanc.retrofit.helper.sample.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.dylanc.retrofit.helper.annotations.ApiUrl
import com.dylanc.retrofit.helper.sample.R
import com.dylanc.retrofit.helper.sample.api.GankApi
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    btn_java.setOnClickListener {
      startActivity(Intent(this, JavaActivity::class.java))
    }
    btn_kotlin_rxjava.setOnClickListener {
      startActivity(Intent(this, KotlinRxJavaActivity::class.java))
    }
    btn_kotlin_coroutines.setOnClickListener {
      startActivity(Intent(this, KotlinCoroutinesActivity::class.java))
    }
  }
}