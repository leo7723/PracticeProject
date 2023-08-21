package com.leo.practiceproject.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.leo.practiceproject.R
import com.leo.practiceproject.databinding.ActivityAccessibilityBinding
import com.leo.practiceproject.databinding.ActivityRecyclerviewBinding

class RecyclerviewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecyclerviewBinding

    private var data = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecyclerviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        data.add("First")
        data.add("Second")
        data.add("Third")
        data.add("Forth")
        data.add("Fifth")
        data.add("Sixth")
        data.add("Seventh")
        data.add("Eighth")
        data.add("Ninth")
        data.add("Tenth")
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = NewsAdapter(data)
    }
}