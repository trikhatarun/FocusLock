package com.trikh.focuslock.widget.extensions

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.trikh.focuslock.R
import java.util.*


class TimeSliderRangePicker @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mThumbStartX: Int = 0
    private var mThumbStartY: Int = 0
    private var mThumbEndX: Int = 0
    private var mThumbEndY: Int = 0
    private var mCircleCenterX: Int = 0
    private var mCircleCenterY: Int = 0
    private var mCircleRadius: Int = 0
    private var mStartThumbImage: Drawable? = null
    private var mEndThumbImage: Drawable? = null
    private var mPadding: Int = 0
    var startThumbSize: Int = 0
        set(thumbSize) {
            if (thumbSize == THUMB_SIZE_NOT_DEFINED)
                return
            field = thumbSize
        }
    var endThumbSize: Int = 0
        set(thumbSize) {
            if (thumbSize == THUMB_SIZE_NOT_DEFINED)
                return
            field = thumbSize
        }
    private var mStartThumbColor: Int = 0
    private var mEndThumbColor: Int = 0
    private var mBorderColor: Int = 0
    private var mBorderThickness: Int = 0
    private var mArcDashSize: Int = 0
    private var mArcColor: Int = 0
    private var mAngleStart: Double = 0.toDouble()
    private var mAngleEnd: Double = 0.toDouble()
    private var mIsThumbSelected = false
    private var mIsThumbEndSelected = false
    private val mPaint = Paint()
    private val mLinePaint = Paint()
    private val arcRectF = RectF()
    private val arcRect = Rect()
    private val paint = Paint()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = Rect()
    var start = Calendar.getInstance()
        private set
    var end = Calendar.getInstance()
        private set
    private var mListener: OnSliderRangeMovedListener? = null

    private var oldX: Int = 0
    private var oldY: Int = 0

    private var arcColorAM: Int = 0
    private var arcColorPM: Int = 0

    private var thumbImageAM: Drawable? = null
    private var thumbEndImageAM: Drawable? = null
    private var thumbImagePM: Drawable? = null
    private var thumbEndImagePM: Drawable? = null


    // draw the ticks
    internal var tickLen = 10
    internal var medTickLen = 20
    internal var longTickLen = 30
    internal var tickColor = 0xCCCCCC

    init {
        init(context, attrs, defStyleAttr)
    }


    // common initializer method
    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.TimeSliderRangePicker,
            defStyleAttr,
            0
        )

        // read all available attributes
        //float startAngle = a.getFloat(R.styleable.CircularSlider_start_angle, 90);
        //float endAngle = a.getFloat(R.styleable.CircularSlider_end_angle, 60);
        val thumbSize = a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_thumb_size, 50)
        var startThumbSize = a.getDimensionPixelSize(
            R.styleable.TimeSliderRangePicker_start_thumb_size,
            THUMB_SIZE_NOT_DEFINED
        )
        var endThumbSize = a.getDimensionPixelSize(
            R.styleable.TimeSliderRangePicker_end_thumb_size,
            THUMB_SIZE_NOT_DEFINED
        )
        val thumbColor = a.getColor(R.styleable.TimeSliderRangePicker_start_thumb_color, Color.GRAY)
        val thumbEndColor =
            a.getColor(R.styleable.TimeSliderRangePicker_end_thumb_color, Color.GRAY)
        val borderThickness =
            a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_border_thickness, 20)
        val arcDashSize =
            a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_arc_dash_size, 60)
        arcColorAM = a.getColor(R.styleable.TimeSliderRangePicker_arc_color_am, 0)
        arcColorPM = a.getColor(R.styleable.TimeSliderRangePicker_arc_color_pm, 0)
        val borderColor = a.getColor(R.styleable.TimeSliderRangePicker_border_color, Color.RED)
        thumbImageAM = a.getDrawable(R.styleable.TimeSliderRangePicker_start_thumb_image_am)
        thumbEndImageAM = a.getDrawable(R.styleable.TimeSliderRangePicker_end_thumb_image_am)
        thumbImagePM = a.getDrawable(R.styleable.TimeSliderRangePicker_start_thumb_image_pm)
        thumbEndImagePM = a.getDrawable(R.styleable.TimeSliderRangePicker_end_thumb_image_pm)

        // save those to fields (really, do we need setters here..?)
        start.set(Calendar.HOUR_OF_DAY, 3)
        start.set(Calendar.MINUTE, 0)
        end.set(Calendar.HOUR_OF_DAY, 6)
        end.set(Calendar.MINUTE, 0)
        setStartAngle(timeToDegrees(start))
        setEndAngle(timeToDegrees(end))
        setBorderThickness(borderThickness)
        setBorderColor(borderColor)
        setThumbSize(thumbSize)
        setStartThumbImage(thumbImageAM)
        setEndThumbImage(thumbEndImageAM)
        setStartThumbColor(thumbColor)
        setEndThumbColor(thumbEndColor)
        updateArcColor()
        setArcDashSize(arcDashSize)

        // assign padding - check for version because of RTL layout compatibility
        val padding: Int

        val all =
            paddingLeft + paddingRight + paddingBottom + paddingTop + paddingEnd + paddingStart
        padding = all / 6

        setPadding(padding)
        a.recycle()

        if (isInEditMode)
            return
    }

    fun setTime(start: Calendar, end: Calendar) {
        this.start = start
        this.end = end
        mAngleStart = fromDrawingAngle(timeToDegrees(start))
        mAngleEnd = fromDrawingAngle(timeToDegrees(end))
        updateArcColor()
        invalidate()
        notifyChanges()
    }

    /**
     * Set start angle by time.
     * An angle of 0 degrees correspond to the geometric angle of 0 degrees (3 o'clock on a watch.)
     *
     * @param time of the time.
     */
    fun setStartTime(time: Calendar) {
        mAngleStart = fromDrawingAngle(timeToDegrees(time))
        invalidate()
    }

    /**
     * Set end angle by time.
     * An angle of 0 degrees correspond to the geometric angle of 0 degrees (3 o'clock on a watch.)
     *
     * @param time of the time.
     */
    fun setEndTime(time: Calendar) {
        mAngleEnd = fromDrawingAngle(timeToDegrees(time))
        invalidate()
    }

    /**
     * Set start angle in degrees.
     * An angle of 0 degrees correspond to the geometric angle of 0 degrees (3 o'clock on a watch.)
     *
     * @param startAngle value in degrees.
     */
    fun setStartAngle(startAngle: Double) {
        mAngleStart = fromDrawingAngle(startAngle)
    }

    /**
     * Set end angle in degrees.
     * An angle of 0 degrees correspond to the geometric angle of 0 degrees (3 o'clock on a watch.)
     *
     * @param angle value in degrees.
     */
    fun setEndAngle(angle: Double) {
        mAngleEnd = fromDrawingAngle(angle)
    }


    fun setThumbSize(thumbSize: Int) {
        startThumbSize = thumbSize
        endThumbSize = thumbSize
    }

    fun setBorderThickness(circleBorderThickness: Int) {
        mBorderThickness = circleBorderThickness
    }

    fun setBorderColor(color: Int) {
        mBorderColor = color
    }

    fun setStartThumbImage(drawable: Drawable?) {
        mStartThumbImage = drawable
    }

    fun setEndThumbImage(drawable: Drawable?) {
        mEndThumbImage = drawable
    }

    fun setStartThumbColor(color: Int) {
        mStartThumbColor = color
    }

    fun setEndThumbColor(color: Int) {
        mEndThumbColor = color
    }

    fun setPadding(padding: Int) {
        mPadding = padding
    }

    fun updateArcColor() {
        if (start.get(Calendar.AM_PM) == Calendar.AM) {
            mArcColor = arcColorAM
            mStartThumbImage = thumbImageAM
            mEndThumbImage = thumbEndImageAM
        } else {
            mArcColor = arcColorPM
            mStartThumbImage = thumbImagePM
            mEndThumbImage = thumbEndImagePM
        }
    }

    fun setArcDashSize(value: Int) {
        mArcDashSize = value
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        // use smaller dimension for calculations (depends on parent size)
        val smallerDim = if (w > h) h else w

        // find circle's rectangle points
        val largestCenteredSquareLeft = (w - smallerDim) / 2
        val largestCenteredSquareTop = (h - smallerDim) / 2
        val largestCenteredSquareRight = largestCenteredSquareLeft + smallerDim
        val largestCenteredSquareBottom = largestCenteredSquareTop + smallerDim

        // save circle coordinates and radius in fields
        mCircleCenterX = largestCenteredSquareRight / 2 + (w - largestCenteredSquareRight) / 2
        mCircleCenterY = largestCenteredSquareBottom / 2 + (h - largestCenteredSquareBottom) / 2
        mCircleRadius = smallerDim / 2 - mBorderThickness / 2 - mPadding

        // works well for now, should we call something else here?
        super.onSizeChanged(w, h, oldw, oldh)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val padding = 10.0
        val r = mCircleRadius.toDouble() - (mBorderThickness / 2).toDouble() - padding
        val cX = mCircleCenterX.toDouble()
        val cY = mCircleCenterY.toDouble()


        // paint.color = Color.WHITE
        // paint.strokeWidth = 2.dip
        /*textPaint.color = Color.BLACK
        textPaint.textSize = 16.dip
        for (i in 1..12) {

            val di = i.toDouble()
            val angleFrom12 = di / 12.0 * 2.0 * Math.PI
            val angleFrom3 = Math.PI / 2.0 - angleFrom12
            //g.rotate((float)angleFrom12, (int)absCX, (int)absCY);

            *//*canvas.drawLine(
                (cX + Math.cos(angleFrom3) * (r - 2.dip)).toFloat(),
                (cY - Math.sin(angleFrom3) * (r - 2.dip)).toFloat(),
                (cX + Math.cos(angleFrom3) * (r - medTickLen - 2.dip)).toFloat(),
                (cY - Math.sin(angleFrom3) * (r - medTickLen - 2.dip)).toFloat(), paint
            ) *//*

            val numStr = "" + i
            textPaint.getTextBounds(numStr, 0, numStr.length, rect)
            val charWidth = rect.width()
            val charHeight = rect.height().toFloat()
            val tx = (cX + Math.cos(angleFrom3) * (r - longTickLen - 16.dip)).toInt()
            val ty = (cY - Math.sin(angleFrom3) * (r - longTickLen - 16.dip)).toInt()

            canvas.drawText(numStr, (tx - charWidth / 2).toFloat(), ty + charHeight / 3, textPaint)
        }*/


        // outer circle (ring)
        mPaint.color = mBorderColor
        //  mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mBorderThickness.toFloat()
        mPaint.isAntiAlias = true
        canvas.drawCircle(
            mCircleCenterX.toFloat(),
            mCircleCenterY.toFloat(),
            mCircleRadius.toFloat(),
            mPaint
        )


        textPaint.color = Color.BLACK
        textPaint.textSize = 16.dip
        for (i in 1..12) {

            val di = i.toDouble()
            val angleFrom12 = di / 12.0 * 2.0 * Math.PI
            val angleFrom3 = Math.PI / 2.0 - angleFrom12
            //g.rotate((float)angleFrom12, (int)absCX, (int)absCY);

            /*canvas.drawLine(
                (cX + Math.cos(angleFrom3) * (r - 2.dip)).toFloat(),
                (cY - Math.sin(angleFrom3) * (r - 2.dip)).toFloat(),
                (cX + Math.cos(angleFrom3) * (r - medTickLen - 2.dip)).toFloat(),
                (cY - Math.sin(angleFrom3) * (r - medTickLen - 2.dip)).toFloat(), paint
            ) */

            val numStr = "" + i
            textPaint.getTextBounds(numStr, 0, numStr.length, rect)
            val charWidth = rect.width()
            val charHeight = rect.height().toFloat()
            val tx = (cX + Math.cos(angleFrom3) * (r - longTickLen - 8.dip)).toInt()
            val ty = (cY - Math.sin(angleFrom3) * (r - longTickLen - 8.dip)).toInt()

            canvas.drawText(numStr, (tx - charWidth / 2).toFloat(), ty + charHeight / 3, textPaint)
        }

        // find thumb start position
        mThumbStartX = (mCircleCenterX + mCircleRadius * Math.cos(mAngleStart)).toInt()
        mThumbStartY = (mCircleCenterY - mCircleRadius * Math.sin(mAngleStart)).toInt()

        //find thumb end position
        mThumbEndX = (mCircleCenterX + mCircleRadius * Math.cos(mAngleEnd)).toInt()
        mThumbEndY = (mCircleCenterY - mCircleRadius * Math.sin(mAngleEnd)).toInt()

        mLinePaint.color = if (mArcColor == 0) Color.RED else mArcColor
        mLinePaint.style = Paint.Style.STROKE
        mLinePaint.strokeWidth = mArcDashSize.toFloat()
        mLinePaint.isAntiAlias = true
        mLinePaint.textSize = 50f

        arcRect.set(
            mCircleCenterX - mCircleRadius,
            mCircleCenterY + mCircleRadius,
            mCircleCenterX + mCircleRadius,
            mCircleCenterY - mCircleRadius
        )
        arcRectF.set(arcRect)
        arcRectF.sort()

        val drawStart = toDrawingAngle(mAngleStart)
        val drawEnd = toDrawingAngle(mAngleEnd)
        val shader = LinearGradient(
            0f,
            0f,
            0f,
            height.toFloat(),
            Color.BLACK,
            if (mArcColor === 0) Color.RED else mArcColor,
            Shader.TileMode.CLAMP
        )
        mLinePaint.shader = shader
        canvas.drawArc(arcRectF, drawStart, (360 + drawEnd - drawStart) % 360, false, mLinePaint)

        mPaint.style = Paint.Style.FILL
        mPaint.color = if (mArcColor == 0) Color.RED else mArcColor
        canvas.drawCircle(
            mThumbStartX.toFloat(),
            mThumbStartY.toFloat(),
            (mArcDashSize / 2).toFloat(),
            mPaint
        )

        mPaint.style = Paint.Style.FILL
        mPaint.color = if (mArcColor == 0) Color.RED else mArcColor
        canvas.drawCircle(
            mThumbEndX.toFloat(),
            mThumbEndY.toFloat(),
            (mArcDashSize / 2).toFloat(),
            mPaint
        )

        var mThumbSize = startThumbSize
        mPaint.color = mStartThumbColor
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(
            mThumbStartX.toFloat(),
            mThumbStartY.toFloat(),
            (mThumbSize / 2).toFloat(),
            mPaint
        )
        if (mStartThumbImage != null) {
            // draw png
            mStartThumbImage!!.setBounds(
                mThumbStartX - mThumbSize / 2,
                mThumbStartY - mThumbSize / 2,
                mThumbStartX + mThumbSize / 2,
                mThumbStartY + mThumbSize / 2
            )
            mStartThumbImage!!.draw(canvas)
        } /*else {
            // draw colored circle
            mPaint.color = mStartThumbColor
            mPaint.style = Paint.Style.FILL
            canvas.drawCircle(
                mThumbStartX.toFloat(),
                mThumbStartY.toFloat(),
                (mThumbSize / 2).toFloat(),
                mPaint
            )

            //helper text, used for debugging
            //mLinePaint.setStrokeWidth(5);
            //canvas.drawText(String.format(Locale.US, "%.1f", drawStart), mThumbStartX - 20, mThumbStartY, mLinePaint);
            //canvas.drawText(String.format(Locale.US, "%.1f", drawEnd), mThumbEndX - 20, mThumbEndY, mLinePaint);
        }*/

        mThumbSize = endThumbSize
        mPaint.style = Paint.Style.FILL
        mPaint.color = mEndThumbColor
        canvas.drawCircle(
            mThumbEndX.toFloat(),
            mThumbEndY.toFloat(),
            (mThumbSize / 2).toFloat(),
            mPaint
        )
        if (mEndThumbImage != null) {
            // draw png
            mEndThumbImage!!.setBounds(
                mThumbEndX - mThumbSize / 2,
                mThumbEndY - mThumbSize / 2,
                mThumbEndX + mThumbSize / 2,
                mThumbEndY + mThumbSize / 2
            )
            mEndThumbImage!!.draw(canvas)
        }/* else {
            mPaint.style = Paint.Style.FILL
            mPaint.color = mEndThumbColor
            canvas.drawCircle(
                mThumbEndX.toFloat(),
                mThumbEndY.toFloat(),
                (mThumbSize / 2).toFloat(),
                mPaint
            )
        }*/


    }

    /**
     * Invoked when slider starts moving or is currently moving. This method calculates and sets position and angle of the thumb.
     *
     * @param touchX Where is the touch identifier now on X axis
     * @param touchY Where is the touch identifier now on Y axis
     */
    private fun updateSliderState(touchX: Int, touchY: Int) {
        val distanceX = touchX - mCircleCenterX
        val distanceY = mCircleCenterY - touchY

        val c = Math.sqrt(Math.pow(distanceX.toDouble(), 2.0) + Math.pow(distanceY.toDouble(), 2.0))
        var angle = Math.acos(distanceX / c)
        if (distanceY < 0) angle = -angle

        val oldDistanceX = oldX - mCircleCenterX
        val oldDistanceY = mCircleCenterY - oldY

        val oldC = Math.sqrt(
            Math.pow(oldDistanceX.toDouble(), 2.0) + Math.pow(
                oldDistanceY.toDouble(),
                2.0
            )
        )
        var oldAngle = Math.acos(oldDistanceX / oldC)
        if (oldDistanceY < 0) oldAngle = -oldAngle

        val gap = angle - oldAngle

        //if(Math.abs(toDrawingAngle(mAngleStart+gap) - toDrawingAngle(mAngleStart)) > 60)return false;

        val oldmAngleStart = mAngleStart
        val oldmAngleEnd = mAngleEnd

        mAngleStart += gap
        mAngleEnd += gap

        degreesToTime(start, toDrawingAngle(mAngleStart).toDouble())
        degreesToTime(end, toDrawingAngle(mAngleEnd).toDouble())

        updateChangedAMPM(start, oldmAngleStart, mAngleStart)
        if (start.get(Calendar.HOUR) + Math.abs(start.get(Calendar.HOUR) - end.get(Calendar.HOUR)) >= 12)
            end.set(
                Calendar.AM_PM,
                if (start.get(Calendar.AM_PM) == Calendar.AM) Calendar.PM else Calendar.AM
            )
        else
            end.set(Calendar.AM_PM, start.get(Calendar.AM_PM))

        oldX = touchX
        oldY = touchY

        notifyChanges()
    }

    private fun updateSliderState(touchX: Int, touchY: Int, thumb: Thumb) {
        val distanceX = touchX - mCircleCenterX
        val distanceY = mCircleCenterY - touchY

        val c = Math.sqrt(Math.pow(distanceX.toDouble(), 2.0) + Math.pow(distanceY.toDouble(), 2.0))
        var angle = Math.acos(distanceX / c)
        if (distanceY < 0)
            angle = -angle


        if (thumb === Thumb.START) {
            val oldmAngleStart = mAngleStart
            mAngleStart = angle
            degreesToTime(start, toDrawingAngle(mAngleStart).toDouble())
            updateChangedAMPM(start, oldmAngleStart, mAngleStart)

        } else {
            val oldmAngleEnd = mAngleEnd
            mAngleEnd = angle
            degreesToTime(end, toDrawingAngle(mAngleEnd).toDouble())
            updateChangedAMPM(end, oldmAngleEnd, mAngleEnd)
        }
        notifyChanges()
    }

    private fun notifyChanges() {
        if (mListener != null) {
            mListener!!.onChange(start, end)
        }
    }

    private fun toDrawingAngle(angleInRadians: Double): Float {
        var fixedAngle = Math.toDegrees(angleInRadians)
        if (angleInRadians > 0)
            fixedAngle = 360 - fixedAngle
        else
            fixedAngle = -fixedAngle
        return fixedAngle.toFloat()
    }

    private fun fromDrawingAngle(angleInDegrees: Double): Double {
        val radians = Math.toRadians(angleInDegrees)
        return -radians
    }

    private fun timeToDegrees(time: Calendar): Double {
        val h = time.get(Calendar.HOUR_OF_DAY).toDouble()
        val m = time.get(Calendar.MINUTE).toDouble()

        return (h * 60.0 + m - 180) / 720 * 360 % 360
    }

    private fun degreesToTime(time: Calendar, degrees: Double) {
        val s = degrees / 360
        val sr = 180 + s * 12 * 60
        time.set(Calendar.HOUR, (sr / 60).toInt() % 12)
        time.set(Calendar.MINUTE, (sr % 60).toInt())
    }

    private fun updateChangedAMPM(time: Calendar, oldAngle: Double, newAngle: Double) {
        val oldDegree = toDrawingAngle(oldAngle).toDouble()
        val newDegree = toDrawingAngle(newAngle).toDouble()
        if (oldDegree > 180 && oldDegree < 270 && newDegree >= 270 && newDegree < 360 || oldDegree < 360 && oldDegree >= 270 && newDegree < 270 && newDegree > 180) {
            time.set(
                Calendar.AM_PM,
                if (time.get(Calendar.AM_PM) == Calendar.AM) Calendar.PM else Calendar.AM
            )
            updateArcColor()
        }
    }


    /**
     * Set slider range moved listener. Set [OnSliderRangeMovedListener] to `null` to remove it.
     *
     * @param listener Instance of the slider range moved listener, or null when removing it
     */
    fun setOnChangeListener(listener: OnSliderRangeMovedListener) {
        mListener = listener
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                // start moving the thumb (this is the first touch)
                val x = ev.x.toInt()
                val y = ev.y.toInt()

                var mThumbSize = startThumbSize
                val isThumbStartPressed = (x < mThumbStartX + mThumbSize
                        && x > mThumbStartX - mThumbSize
                        && y < mThumbStartY + mThumbSize
                        && y > mThumbStartY - mThumbSize)

                mThumbSize = endThumbSize
                val isThumbEndPressed = (x < mThumbEndX + mThumbSize
                        && x > mThumbEndX - mThumbSize
                        && y < mThumbEndY + mThumbSize
                        && y > mThumbEndY - mThumbSize)

                if (isThumbStartPressed) {
                    mIsThumbSelected = true
                    updateSliderState(x, y, Thumb.START)
                } else if (isThumbEndPressed) {
                    mIsThumbEndSelected = true
                    updateSliderState(x, y, Thumb.END)
                } else {
                    oldX = x
                    oldY = y
                }
            }

            MotionEvent.ACTION_MOVE -> {
                // still moving the thumb (this is not the first touch)
                if (mIsThumbSelected) {
                    val x = ev.x.toInt()
                    val y = ev.y.toInt()
                    updateSliderState(x, y, Thumb.START)
                } else if (mIsThumbEndSelected) {
                    val x = ev.x.toInt()
                    val y = ev.y.toInt()
                    updateSliderState(x, y, Thumb.END)
                } else {
                    val x = ev.x.toInt()
                    val y = ev.y.toInt()
                    updateSliderState(x, y)
                }
            }

            MotionEvent.ACTION_UP -> {
                mIsThumbSelected = false
                mIsThumbEndSelected = false
            }
        }

        invalidate()
        return true
    }

    companion object {

        private val THUMB_SIZE_NOT_DEFINED = -1
    }

    enum class Thumb {
        START, END
    }

    private val Int.dip: Float
        get() {
            return resources.displayMetrics.density.times(this)
        }

    public interface OnSliderRangeMovedListener {
        fun onChange(start: Calendar, end: Calendar)
    }
}
