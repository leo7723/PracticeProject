package com.leo.practiceproject.accessbility

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import com.google.android.material.snackbar.Snackbar
import com.leo.practiceproject.R
import com.leo.practiceproject.databinding.ActivityAccessibilityBinding

class AccessibilityActivity : AppCompatActivity() {

    // 类名来自layout文件
    private lateinit var binding: ActivityAccessibilityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAccessibilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding.toolbarLayout.title = title
        binding.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        var scrollDirection = 1
        binding.floatButton.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onRequestSendAccessibilityEvent(
                host: ViewGroup?,
                child: View?,
                event: AccessibilityEvent?
            ): Boolean {
                return super.onRequestSendAccessibilityEvent(host, child, event)
            }

            override fun onPopulateAccessibilityEvent(host: View?, event: AccessibilityEvent?) {
                super.onPopulateAccessibilityEvent(host, event)
            }

            override fun performAccessibilityAction(
                host: View?,
                action: Int,
                args: Bundle?
            ): Boolean {
                if(binding.include.nestedScroll.canScrollVertically(1)) {
                    binding.include.nestedScroll.scrollBy(0, 400 * scrollDirection)
                    return binding.include.nestedScroll.performAccessibilityAction(action, args)
                }
                scrollDirection = -scrollDirection
                return super.performAccessibilityAction(host, action, args)
            }
        }
        binding.include.nestedScroll.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onRequestSendAccessibilityEvent(
                host: ViewGroup?,
                child: View?,
                event: AccessibilityEvent?
            ): Boolean {

//                if (binding.include.nestedScroll.canScrollVertically(-1) xor binding.include.nestedScroll.canScrollVertically(1)) {
//                    Log.i("leo7723", "scrollDirection -1 can " + binding.include.nestedScroll.canScrollVertically(-1))
//                    Log.i("leo7723", "scrollDirection 1 can " + binding.include.nestedScroll.canScrollVertically(1))
//                    scrollDirection = if (binding.include.nestedScroll.canScrollVertically(1)) 1 else -1
//                    Log.i("leo7723", "scrollDirection " + scrollDirection)
//                }


                return super.onRequestSendAccessibilityEvent(host, child, event)
            }

            override fun onPopulateAccessibilityEvent(host: View?, event: AccessibilityEvent?) {
                super.onPopulateAccessibilityEvent(host, event)
            }

            override fun performAccessibilityAction(
                host: View?,
                action: Int,
                args: Bundle?
            ): Boolean {
                if(host is NestedScrollView && host.canScrollVertically(1)) {}
                return super.performAccessibilityAction(host, action, args)
            }
        }

        // 嵌套滑动Scroll
        binding.include.nestedLinear.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onRequestSendAccessibilityEvent(
                host: ViewGroup?,
                child: View?,
                event: AccessibilityEvent?
            ): Boolean {
                return super.onRequestSendAccessibilityEvent(host, child, event)
            }

            override fun onPopulateAccessibilityEvent(host: View?, event: AccessibilityEvent?) {
                super.onPopulateAccessibilityEvent(host, event)
            }

            override fun performAccessibilityAction(
                host: View?,
                action: Int,
                args: Bundle?
            ): Boolean {
                return super.performAccessibilityAction(host, action, args)
            }
        }
        binding.root.accessibilityDelegate = object : View.AccessibilityDelegate() {
            override fun onRequestSendAccessibilityEvent(
                host: ViewGroup?,
                child: View?,
                event: AccessibilityEvent?
            ): Boolean {
                return super.onRequestSendAccessibilityEvent(host, child, event)
            }

            override fun onPopulateAccessibilityEvent(host: View?, event: AccessibilityEvent?) {
                super.onPopulateAccessibilityEvent(host, event)
            }

            override fun performAccessibilityAction(
                host: View?,
                action: Int,
                args: Bundle?
            ): Boolean {
                return super.performAccessibilityAction(host, action, args)
            }
        }
    }
}