package com.lacolinares.digidraw.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

class HandWrittenDigitView(context: Context) : View(context) {

    private lateinit var mPath: Path
    private lateinit var mPaint: Paint
    private lateinit var mCanvasPaint: Paint
    private lateinit var mCanvas: Canvas
    private lateinit var mBitmap: Bitmap
    private val mAllPoints = arrayListOf<List<Pair<Float, Float>>>()
    private val mConsecutivePoints = arrayListOf<Pair<Float, Float>>()

    init {
        setPathPaint()
    }

    private fun setPathPaint() {
        mPath = Path()
        mPaint = Paint().apply {
            color = Color.WHITE
            isAntiAlias = true
            strokeWidth = 32f
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
        }
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.let {
            it.drawBitmap(mBitmap, 0f, 0f, mCanvasPaint)
            it.drawPath(mPath, mPaint)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mConsecutivePoints.clear()
                mConsecutivePoints.add(Pair(x, y))
                mPath.moveTo(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                mConsecutivePoints.add(Pair(x, y))
                mPath.lineTo(x, y)
            }
            MotionEvent.ACTION_UP -> {
                mConsecutivePoints.add(Pair(x, y))
                mAllPoints.add(ArrayList(mConsecutivePoints))
                mCanvas.drawPath(mPath, mPaint)
                mPath.reset()
            }
            else -> return false
        }

        invalidate()
        return true
    }

    fun getAllPoints(): List<List<Pair<Float, Float>>> {
        return mAllPoints
    }

    fun clearAllPointsAndRedraw() {
        mBitmap = Bitmap.createBitmap(mBitmap.width, mBitmap.height, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
        mCanvasPaint = Paint(Paint.DITHER_FLAG)
        mCanvas.drawBitmap(mBitmap, 0f, 0f, mCanvasPaint)
        setPathPaint()
        invalidate()
        mAllPoints.clear()
    }

    fun clearAllPoints() {
        mAllPoints.clear()
    }

}