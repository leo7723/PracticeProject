package com.leo.practiceproject.today

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.accessibility.AccessibilityEvent
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import com.leo.practiceproject.R
import com.leo.practiceproject.transition.CustomSharedElementCallback

class TransformFirstActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        // Set up shared element transition and disable overlay so views don't show above system bars
//        var callback = MaterialContainerTransformSharedElementCallback()
        var callback = CustomSharedElementCallback()
        setExitSharedElementCallback(callback)
//        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transform_first)

        var cardView = findViewById<MaterialCardView>(R.id.vertical_card_item)
        cardView.setOnClickListener {
            val intent = Intent(this, TransformSecActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this, cardView, "shared_element_end_root"
            )
            startActivity(intent, options.toBundle())
//            startActivity(intent)
        }

        var gridCardView = findViewById<MaterialCardView>(R.id.grid_card_item)
        gridCardView.setOnClickListener {
            val intent = Intent(this, TransformSecActivity::class.java)
            val options = ActivityOptions.makeSceneTransitionAnimation(
                this, gridCardView, "shared_element_end_root"
            )
            startActivity(intent, options.toBundle())
        }
    }
}