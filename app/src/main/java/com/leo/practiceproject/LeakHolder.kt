package com.leo.practiceproject

import android.app.Activity
import com.leo.practiceproject.LeakHolder

class LeakHolder private constructor() {
    private var activity: Activity? = null
    fun setActivity(activity: Activity?) {
        this.activity = activity
    }

    companion object {
        val instance = LeakHolder()
    }
}