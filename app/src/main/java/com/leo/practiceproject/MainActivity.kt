package com.leo.practiceproject

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.leo.practiceproject.accessbility.AccessibilityActivity
import com.leo.practiceproject.recyclerview.RecyclerviewActivity
import com.leo.practiceproject.today.TransformFirstActivity
import com.leo.practiceproject.today.TransformSecActivity
import com.leo.practiceproject.touchlearn.TouchButton
import com.leo.practiceproject.touchlearn.TouchTextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var touchButton = findViewById<TouchButton>(R.id.touch_button2)
        touchButton.setOnClickListener {
            Log.i("leo7723", "touchButton-OnClickListener")
            val intent = Intent(this, TransformFirstActivity::class.java)
            startActivity(intent)
        }

        findViewById<TouchButton>(R.id.touch_button3)
            .setOnClickListener {
                Log.i("leo7723", "touchButton-OnClickListener")
                val intent = Intent(this, AccessibilityActivity::class.java)
                startActivity(intent)
            }

        findViewById<TouchButton>(R.id.touch_button4)
            .setOnClickListener {
                Log.i("leo7723", "touchButton-OnClickListener")
                val intent = Intent(this, RecyclerviewActivity::class.java)
                startActivity(intent)
            }

        findViewById<TouchTextView>(R.id.touch_text_view)
            .setOnClickListener {Log.i("leo7723", "touch_text_view-OnClickListener")}
    }
}