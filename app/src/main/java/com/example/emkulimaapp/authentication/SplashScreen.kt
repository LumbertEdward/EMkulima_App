package com.example.emkulimaapp.authentication

import android.content.Intent
import android.content.res.Resources
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.example.emkulimaapp.MainActivity
import com.example.emkulimaapp.R

class SplashScreen : AppCompatActivity() {
    @BindView(R.id.txtName)
    lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        ButterKnife.bind(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            window.navigationBarColor = resources.getColor(R.color.green, Resources.getSystem().newTheme())
        }

        val animation: Animation = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left)
        textView.startAnimation(animation)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent: Intent = Intent(this, Login::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            finish()
        }, 3000)
    }
}