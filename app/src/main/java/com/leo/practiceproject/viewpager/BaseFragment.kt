package com.leo.practiceproject.viewpager

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    companion object {
        private const val TAG = "BaseFragment"
    }

    protected var hasDataLoaded = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i(TAG, "${javaClass.simpleName} onViewCreated")

    }
}