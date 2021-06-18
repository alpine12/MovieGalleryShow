package com.alpine12.moviegalleryshow.ui.splash


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.alpine12.moviegalleryshow.R
import com.alpine12.moviegalleryshow.ui.main.MainActivity
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
//            delay(2000L)
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }
    }
}