package com.zekikos.pinchzoomandpan

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View

class PinchZoomAndPanView(context: Context, attrs: AttributeSet) : View(context, attrs), View.OnTouchListener, ScaleGestureDetector.OnScaleGestureListener{
    private var imageBitmap : Bitmap? = null
    private var scaleGestureDetector: ScaleGestureDetector = ScaleGestureDetector(context, this);


    private val mMatrix = Matrix()
    private val mPaint = Paint()

    init{
        setOnTouchListener(this);
    }

    fun setImage(bitmap: Bitmap){
        this.imageBitmap = bitmap
        invalidate()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.setMatrix(mMatrix)
        imageBitmap?.let { canvas?.drawBitmap(it, (width - it.width) / 2f, (height - it.height) / 2f, mPaint) }
    }

    private var lastPosition: List<Float> = listOf(0f, 0f);
    override fun onTouch(view: View?, motionEvent: MotionEvent?): Boolean {
        scaleGestureDetector.onTouchEvent(motionEvent)
        when(motionEvent?.actionMasked){
            MotionEvent.ACTION_POINTER_DOWN->{
                val centerX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2
                val centerY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2
                lastPosition = listOf(centerX, centerY)
            }
            MotionEvent.ACTION_MOVE->{
                if(motionEvent.pointerCount == 2){
                    val centerX = (motionEvent.getX(0) + motionEvent.getX(1)) / 2
                    val centerY = (motionEvent.getY(0) + motionEvent.getY(1)) / 2
                    mMatrix.postTranslate(centerX - lastPosition[0],  centerY - lastPosition[1])
                    lastPosition = listOf(centerX, centerY)
                    invalidate()
                }
            }
        }
        return true
    }

    override fun onScale(detector: ScaleGestureDetector?): Boolean {
        if(detector == null) return false
        mMatrix.postScale(detector.scaleFactor, detector.scaleFactor, lastPosition[0], lastPosition[1])
        invalidate()
        return true
    }

    override fun onScaleBegin(p0: ScaleGestureDetector?): Boolean {
       return true
    }

    override fun onScaleEnd(p0: ScaleGestureDetector?) {
    }
}
