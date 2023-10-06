package com.leo.practiceproject.transition

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.*
import android.graphics.drawable.Drawable
import android.transition.PathMotion
import android.transition.Transition
import android.transition.TransitionValues
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.core.view.ViewCompat

class CustomTransition : Transition() {
    lateinit var currentStartBounds : RectF
    lateinit var currentEndBounds : RectF

    private companion object {
        const val TAG = "CustomTransition"
        const val PROP_BOUNDS = "transition:bounds"
    }
    override fun captureStartValues(transitionValues: TransitionValues?) {
        Log.i(TAG, "captureStartValues")
        if(transitionValues == null) {
            return
        }
        val view = transitionValues.view
        if (ViewCompat.isLaidOut(view) || view.width != 0 || view.height != 0) {
            var bounds = view.parent?.let { getRelativeBounds(view) } ?: run { getLocationOnScreen(view) }
            transitionValues.values[PROP_BOUNDS] = bounds
            currentStartBounds = RectF(bounds)
        }
    }

    override fun captureEndValues(transitionValues: TransitionValues?) {
        Log.i(TAG, "captureEndValues")
        if(transitionValues == null) {
            return
        }
        val view = transitionValues.view
        if (ViewCompat.isLaidOut(view) || view.width != 0 || view.height != 0) {
            var bounds = view.parent?.let { getRelativeBounds(view) } ?: run { getLocationOnScreen(view) }
            transitionValues.values[PROP_BOUNDS] = bounds
            currentEndBounds = RectF(bounds)
        }
    }

    fun findAncestorById(view: View?, @IdRes ancestorId: Int): View? {
        var view = view
        val resourceName = view!!.resources.getResourceName(ancestorId)
        while (view != null) {
            if (view.id == ancestorId) {
                return view
            }
            val parent = view.parent
            view = if (parent is View) {
                parent
            } else {
                break
            }
        }
        return view
    }

    override fun createAnimator(
        sceneRoot: ViewGroup?,
        startValues: TransitionValues?,
        endValues: TransitionValues?
    ): Animator {
        if (startValues == null || endValues == null) {
            return super.createAnimator(sceneRoot, startValues, endValues)
        }
        val customDrawable = CustomDrawable(startValues, endValues)
        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.addUpdateListener { animation ->
            Log.i(TAG, "animation.animatedFraction ${animation.animatedFraction}")
            customDrawable.updateProgress(animation.animatedFraction)
        }

        val baseView = findAncestorById(endValues.view, android.R.id.content)
            ?: return super.createAnimator(sceneRoot, startValues, endValues)
        addListener(object : TransitionListener {
            override fun onTransitionStart(transition: Transition?) {

                // Hide the actual views at the beginning of the transition
                startValues.view.setAlpha(0f)
                endValues.view.setAlpha(0f)
                baseView.setAlpha(0f)
                baseView.overlay.add(customDrawable)
            }

            override fun onTransitionEnd(transition: Transition?) {
                startValues.view.setAlpha(1f)
                endValues.view.setAlpha(1f)
                baseView.setAlpha(1f)
                baseView.overlay.remove(customDrawable)
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionResume(transition: Transition?) {
            }
        })
        return animator
    }

    class CustomDrawable : Drawable {

        private val STRAIGHT_PATH_MOTION: PathMotion = object : PathMotion() {
            override fun getPath(startX: Float, startY: Float, endX: Float, endY: Float): Path {
                val path = Path()
                path.moveTo(startX, startY)
                path.lineTo(endX, endY)
                return path
            }
        }
        private var motionPathLength = 0f
        private val motionPathMeasure: PathMeasure
        private var progress = 0f
        private var startPoint : PointF
        private var endPoint : PointF
        private var startBounds: RectF
        private var endBounds: RectF
        private var currentStartBounds: RectF
        private var currentEndBounds: RectF
        private var scaleMax : Float
        private val motionPathPosition = FloatArray(2)
        private val startView : View
        private val endView : View
        private val transformAlphaRectF = RectF()
        private var transformProgress = TransformProgress(0f, 1f, 1f, 0f)

        constructor(startValues: TransitionValues,
                    endValues: TransitionValues) {
            startView = startValues.view
            endView = endValues.view
            // 画图形大小控制
            startBounds = RectF(startValues.values[PROP_BOUNDS] as RectF)
            endBounds = RectF(endValues.values[PROP_BOUNDS] as RectF)
            startPoint = getMotionPathPoint(startBounds)
            endPoint = getMotionPathPoint(endBounds)
            currentStartBounds = RectF(startBounds)
            currentEndBounds = RectF(endBounds)
            scaleMax = (endBounds.width() * endBounds.height()) /
                    (startBounds.width() * startBounds.height())
            val motionPath: Path =
                STRAIGHT_PATH_MOTION.getPath(startPoint.x, startPoint.y, endPoint.x, endPoint.y)
            motionPathMeasure = PathMeasure(motionPath, false)
            motionPathLength = motionPathMeasure.length
            transformProgress = TransformProgress(0f, 1f, 1f, 0f)

            motionPathPosition[0] = startBounds.centerX()
            motionPathPosition[1] = startBounds.top
            updateProgress(0f)
        }

        override fun draw(canvas: Canvas) {
            Log.i(TAG, "draw")
            drawStartView(canvas)
            drawEndView(canvas)
        }

        private fun drawEndView(canvas: Canvas) {
            transform(
                canvas,
                bounds,
                currentEndBounds.left,
                currentEndBounds.top,
                transformProgress.scale,
                transformProgress.alphaEnd,
                object : CanvasOperation {
                    override fun run(canvas: Canvas?) {
                        endView.draw(canvas)
                    }
                })
        }

        private fun drawStartView(canvas: Canvas) {
            transform(
                canvas,
                bounds,
                currentStartBounds.left,
                currentStartBounds.top,
                transformProgress.scale,
                transformProgress.alphaStart,
                object : CanvasOperation {
                    override fun run(canvas: Canvas?) {
                        startView.draw(canvas)
                    }
                })
        }

        fun transform(
            canvas: Canvas,
            bounds: Rect,
            dx: Float,
            dy: Float,
            scale: Float,
            alpha: Float,
            op: CanvasOperation
        ) {
            // Exit early and avoid drawing if what will be drawn is completely transparent.
            if (alpha <= 0) {
                return
            }
            val checkpoint = canvas.save()
            canvas.translate(dx, dy)
            canvas.scale(scale, scale)
            transformAlphaRectF.set(bounds)
            canvas.saveLayerAlpha(transformAlphaRectF, alpha.toInt())
            op.run(canvas)
            canvas.restoreToCount(checkpoint)
        }

        override fun setAlpha(alpha: Int) {
        }

        override fun setColorFilter(colorFilter: ColorFilter?) {
        }

        override fun getOpacity(): Int {
            return PixelFormat.UNKNOWN
        }

        interface CanvasOperation {
            fun run(canvas: Canvas?)
        }

        fun updateProgress(progress: Float) {
            this.progress = progress
            transformProgress.progress = progress
            transformProgress.scale = scaleMax * progress
            transformProgress.alphaStart = 1 - progress
            transformProgress.alphaEnd = progress


            // Calculate position based on motion path
            motionPathMeasure.getPosTan(motionPathLength * progress, motionPathPosition, null)
            val motionPathX = motionPathPosition[0]
            val motionPathY = motionPathPosition[1]
            currentStartBounds.set(
                motionPathX - startBounds.width() * transformProgress.scale / 2,
                motionPathY,
                motionPathX + startBounds.width() * transformProgress.scale / 2,
                motionPathY + startBounds.height() * transformProgress.scale
            )
            currentEndBounds.set(
                motionPathX - startBounds.width() / transformProgress.scale / 2,
                motionPathY,
                motionPathX + startBounds.width() / transformProgress.scale / 2,
                motionPathY + startBounds.height() / transformProgress.scale
            )
            invalidateSelf()

        }

        private fun getMotionPathPoint(bounds: RectF): PointF {
            return PointF(bounds.centerX(), bounds.top)
        }
    }

    data class TransformProgress(
        var progress: Float, var scale: Float,
        var alphaStart: Float, var alphaEnd: Float)

    fun getLocationOnScreen(view: View): RectF {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.width
        val bottom = top + view.height
        return RectF(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat())
    }

    fun getRelativeBounds(view: View): RectF {
        return RectF(
            view.left.toFloat(), view.top.toFloat(), view.right.toFloat(),
            view.bottom.toFloat()
        )
    }


}