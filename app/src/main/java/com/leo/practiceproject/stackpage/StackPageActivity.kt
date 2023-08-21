package com.leo.practiceproject.stackpage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.leo.practiceproject.R

class StackPageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stack_page)
        var viewPager = findViewById<ViewPager2>(R.id.view_pager)

    }
}