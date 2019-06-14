package com.trikh.focuslock.widget.timepicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.res.ResourcesCompat;

import com.trikh.focuslock.R;

import java.util.Calendar;

@SuppressWarnings("IntegerDivisionInFloatingPointContext")
public class TimeSliderRangePicker extends View {

    private static final int THUMB_SIZE_NOT_DEFINED = -1;

    private int mThumbStartX;
    private int mThumbStartY;
    private int mThumbEndX;
    private int mThumbEndY;
    private int mCircleCenterX;
    private int mCircleCenterY;
    private int mCircleRadius;
    private Drawable mStartThumbImage;
    private Drawable mEndThumbImage;
    private int mPadding;
    private int mStartThumbSize;
    private int mEndThumbSize;
    private int mStartThumbColor;
    private int mEndThumbColor;
    private int mBorderColor;
    private int mBackgroundColor;
    private int mBorderThickness;
    private int mArcDashSize;
    private int mArcColor;
    private double mAngleStart;
    private double mAngleEnd;
    private boolean mIsThumbSelected = false;
    private boolean mIsThumbEndSelected = false;
    private Paint mPaint = new Paint();
    private Paint mLinePaint = new Paint();
    private RectF arcRectF = new RectF();
    private Rect arcRect = new Rect();
    private Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Rect rect = new Rect();
    private Calendar start = Calendar.getInstance();
    private Calendar end = Calendar.getInstance();
    private OnSliderRangeMovedListener mListener;
    private int arcColorAM;
    private int arcColorPM;
    private Drawable thumbImageAM;
    private Drawable thumbEndImageAM;
    private Drawable thumbImagePM;
    private Drawable thumbEndImagePM;
    int longTickLen = 30;
    private int dip16 = (int) getResources().getDisplayMetrics().density * 16;

    public TimeSliderRangePicker(Context context) {
        this(context, null);
    }

    public TimeSliderRangePicker(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeSliderRangePicker(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TimeSliderRangePicker, defStyleAttr, 0);

        int thumbSize = a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_thumb_size, 50);
        int startThumbSize = a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_start_thumb_size, THUMB_SIZE_NOT_DEFINED);
        int endThumbSize = a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_end_thumb_size, THUMB_SIZE_NOT_DEFINED);
        int thumbColor = a.getColor(R.styleable.TimeSliderRangePicker_start_thumb_color, Color.GRAY);
        int thumbEndColor = a.getColor(R.styleable.TimeSliderRangePicker_end_thumb_color, Color.GRAY);
        int borderThickness = a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_border_thickness, 20);
        int arcDashSize = a.getDimensionPixelSize(R.styleable.TimeSliderRangePicker_arc_dash_size, 60);
        arcColorAM = a.getColor(R.styleable.TimeSliderRangePicker_arc_color_am, 0);
        arcColorPM = a.getColor(R.styleable.TimeSliderRangePicker_arc_color_pm, 0);
        int borderColor = a.getColor(R.styleable.TimeSliderRangePicker_border_color, Color.RED);
        int backgroundColor = a.getColor(R.styleable.TimeSliderRangePicker_background_color,Color.RED);
        thumbImageAM = a.getDrawable(R.styleable.TimeSliderRangePicker_start_thumb_image_am);
        thumbEndImageAM = a.getDrawable(R.styleable.TimeSliderRangePicker_end_thumb_image_am);
        thumbImagePM = a.getDrawable(R.styleable.TimeSliderRangePicker_start_thumb_image_pm);
        thumbEndImagePM = a.getDrawable(R.styleable.TimeSliderRangePicker_end_thumb_image_pm);

        start.set(Calendar.HOUR_OF_DAY, 3);
        start.set(Calendar.MINUTE, 0);
        end.set(Calendar.HOUR_OF_DAY, 6);
        end.set(Calendar.MINUTE, 0);
        setStartAngle(timeToDegrees(start));
        setEndAngle(timeToDegrees(end));
        setBorderThickness(borderThickness);
        setBorderColor(borderColor);
        mBackgroundColor = backgroundColor;
        setThumbSize(thumbSize);
        setStartThumbSize(startThumbSize);
        setEndThumbSize(endThumbSize);
        setStartThumbImage(thumbImageAM);
        setEndThumbImage(thumbEndImageAM);
        setStartThumbColor(thumbColor);
        setEndThumbColor(thumbEndColor);
        updateArcColor();
        setArcDashSize(arcDashSize);

        int padding;
        int all = getPaddingLeft() + getPaddingRight() + getPaddingBottom() + getPaddingTop() + getPaddingEnd() + getPaddingStart();
        padding = all / 6;
        setPadding(padding);
        a.recycle();
    }

    public void setTime(Calendar start, Calendar end) {
        this.start = start;
        this.end = end;
        mAngleStart = fromDrawingAngle(timeToDegrees(start));
        mAngleEnd = fromDrawingAngle(timeToDegrees(end));
        updateArcColor();
        invalidate();
        notifyChanges();
    }

    public void setStartAngle(double startAngle) {
        mAngleStart = fromDrawingAngle(startAngle);
    }

    public void setEndAngle(double angle) {
        mAngleEnd = fromDrawingAngle(angle);
    }

    public void setThumbSize(int thumbSize) {
        setStartThumbSize(thumbSize);
        setEndThumbSize(thumbSize);
    }

    public int getStartThumbSize() {
        return mStartThumbSize;
    }

    public void setStartThumbSize(int thumbSize) {
        if (thumbSize == THUMB_SIZE_NOT_DEFINED)
            return;
        mStartThumbSize = thumbSize;
    }

    public int getEndThumbSize() {
        return mEndThumbSize;
    }

    public void setEndThumbSize(int thumbSize) {
        if (thumbSize == THUMB_SIZE_NOT_DEFINED)
            return;
        mEndThumbSize = thumbSize;
    }

    public void setBorderThickness(int circleBorderThickness) {
        mBorderThickness = circleBorderThickness;
    }

    public void setBorderColor(int color) {
        mBorderColor = color;
    }
    public void setStartThumbImage(Drawable drawable) {
        mStartThumbImage = drawable;
    }

    public void setEndThumbImage(Drawable drawable) {
        mEndThumbImage = drawable;
    }

    public void setStartThumbColor(int color) {
        mStartThumbColor = color;
    }

    public void setEndThumbColor(int color) {
        mEndThumbColor = color;
    }

    public void setPadding(int padding) {
        mPadding = padding;
    }

    public void updateArcColor() {
        if (start.get(Calendar.AM_PM) == Calendar.AM) {
            mArcColor = arcColorAM;
            mStartThumbImage = thumbImageAM;
            mEndThumbImage = thumbEndImageAM;
        } else {
            mArcColor = arcColorPM;
            mStartThumbImage = thumbImagePM;
            mEndThumbImage = thumbEndImagePM;
        }
    }

    public void setArcDashSize(int value) {
        mArcDashSize = value;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int smallerDim = w > h ? h : w;

        int largestCenteredSquareLeft = (w - smallerDim) / 2;
        int largestCenteredSquareTop = (h - smallerDim) / 2;
        int largestCenteredSquareRight = largestCenteredSquareLeft + smallerDim;
        int largestCenteredSquareBottom = largestCenteredSquareTop + smallerDim;

        mCircleCenterX = largestCenteredSquareRight / 2 + (w - largestCenteredSquareRight) / 2;
        mCircleCenterY = largestCenteredSquareBottom / 2 + (h - largestCenteredSquareBottom) / 2;
        mCircleRadius = smallerDim / 2 - mBorderThickness / 2 - mPadding;

        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        double padding = 10;
        double r = mCircleRadius - mBorderThickness / 2 - padding;
        double cX = mCircleCenterX;
        double cY = mCircleCenterY;

        // background
        mPaint.setColor(mBackgroundColor);
        mPaint.setStrokeWidth(mBorderThickness);
        mPaint.setAntiAlias(true);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mPaint);

        // outer circle (ring)
        mPaint.setColor(mBorderColor);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mCircleRadius, mPaint);

        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize(dip16);
        textPaint.setTypeface(ResourcesCompat.getFont(getContext(),R.font.muli_regular));
        for (int i = 1; i <= 12; i++) {
            double di = (double) i;
            double angleFrom12 = di / 12.0 * 2.0 * Math.PI;
            double angleFrom3 = Math.PI / 2.0 - angleFrom12;

            String numStr = "" + i;
            textPaint.getTextBounds(numStr, 0, numStr.length(), rect);
            int charWidth = rect.width();
            float charHeight = rect.height();
            int tx = (int) (cX + Math.cos(angleFrom3) * (r - longTickLen - dip16));
            int ty = (int) (cY - Math.sin(angleFrom3) * (r - longTickLen - dip16));

            canvas.drawText(numStr, (float) (tx - charWidth / 2), ty + charHeight / 3, textPaint);
        }

        // find thumb start position
        mThumbStartX = (int) (mCircleCenterX + mCircleRadius * Math.cos(mAngleStart));
        mThumbStartY = (int) (mCircleCenterY - mCircleRadius * Math.sin(mAngleStart));

        //find thumb end position
        mThumbEndX = (int) (mCircleCenterX + mCircleRadius * Math.cos(mAngleEnd));
        mThumbEndY = (int) (mCircleCenterY - mCircleRadius * Math.sin(mAngleEnd));

        mLinePaint.setColor(mArcColor == 0 ? Color.RED : mArcColor);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(mArcDashSize);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setTextSize(50);

        arcRect.set(mCircleCenterX - mCircleRadius, mCircleCenterY + mCircleRadius, mCircleCenterX + mCircleRadius, mCircleCenterY - mCircleRadius);
        arcRectF.set(arcRect);
        arcRectF.sort();

        final float drawStart = toDrawingAngle(mAngleStart);
        final float drawEnd = toDrawingAngle(mAngleEnd);

        canvas.drawArc(arcRectF, drawStart, (360 + drawEnd - drawStart) % 360, false, mLinePaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mArcColor == 0 ? Color.RED : mArcColor);
        canvas.drawCircle(mThumbStartX, mThumbStartY, mArcDashSize / 2, mPaint);

        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mArcColor == 0 ? Color.RED : mArcColor);
        canvas.drawCircle(mThumbEndX, mThumbEndY, mArcDashSize / 2, mPaint);

        int mThumbSize = getStartThumbSize();
        mPaint.setColor(mStartThumbColor);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(mThumbStartX, mThumbStartY, mThumbSize / 2, mPaint);
        if (mStartThumbImage != null) {
            // draw png
            mStartThumbImage.setBounds(mThumbStartX - mThumbSize / 2, mThumbStartY - mThumbSize / 2, mThumbStartX + mThumbSize / 2, mThumbStartY + mThumbSize / 2);
            mStartThumbImage.draw(canvas);
        }

        mThumbSize = getEndThumbSize();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mEndThumbColor);
        canvas.drawCircle(mThumbEndX, mThumbEndY, mThumbSize / 2, mPaint);
        if (mEndThumbImage != null) {
            // draw png
            mEndThumbImage.setBounds(mThumbEndX - mThumbSize / 2, mThumbEndY - mThumbSize / 2, mThumbEndX + mThumbSize / 2, mThumbEndY + mThumbSize / 2);
            mEndThumbImage.draw(canvas);
        }
    }

    private void updateSliderState(int touchX, int touchY, Thumb thumb) {
        int distanceX = touchX - mCircleCenterX;
        int distanceY = mCircleCenterY - touchY;
        double c = Math.sqrt(Math.pow(distanceX, 2) + Math.pow(distanceY, 2));
        double angle = Math.acos(distanceX / c);
        if (distanceY < 0)
            angle = -angle;


        if (thumb == Thumb.START) {
            double oldmAngleStart = mAngleStart;
            mAngleStart = angle;
            degreesToTime(start, toDrawingAngle(mAngleStart));
            updateChangedAMPM(start, oldmAngleStart, mAngleStart);

        } else {
            double oldmAngleEnd = mAngleEnd;
            mAngleEnd = angle;
            degreesToTime(end, toDrawingAngle(mAngleEnd));
            updateChangedAMPM(end, oldmAngleEnd, mAngleEnd);
        }
        notifyChanges();
    }

    private void notifyChanges() {
        if (mListener != null) {
            roundTo5(start);
            roundTo5(end);
            end.set(Calendar.DATE,start.get(Calendar.DATE));
            mListener.onChange(start, end);
        }
    }

    void roundTo5(Calendar calendar) {
        int m = calendar.get(Calendar.MINUTE);
        m = Math.round(m / 10) * 10;
        calendar.set(Calendar.MINUTE, m);
    }

    private float toDrawingAngle(double angleInRadians) {
        double fixedAngle = Math.toDegrees(angleInRadians);
        if (angleInRadians > 0)
            fixedAngle = 360 - fixedAngle;
        else
            fixedAngle = -fixedAngle;
        return (float) fixedAngle;
    }

    private double fromDrawingAngle(double angleInDegrees) {
        double radians = Math.toRadians(angleInDegrees);
        return -radians;
    }

    private double timeToDegrees(Calendar time) {
        double h = time.get(Calendar.HOUR_OF_DAY);
        double m = time.get(Calendar.MINUTE);

        return (((h * 60.0 + m - 180) / 720) * 360) % 360;
    }

    private void degreesToTime(Calendar time, double degrees) {
        double s = degrees / 360;
        double sr = 180 + (s * 12) * 60;
        time.set(Calendar.HOUR, (int) (sr / 60) % 12);
        time.set(Calendar.MINUTE, (int) (sr % 60));
    }

    private void updateChangedAMPM(Calendar time, double oldAngle, double newAngle) {
        double oldDegree = toDrawingAngle(oldAngle);
        double newDegree = toDrawingAngle(newAngle);
        if ((oldDegree > 180 && oldDegree < 270) && (newDegree >= 270 && newDegree < 360) ||
                (oldDegree < 360 && oldDegree >= 270) && (newDegree < 270 && newDegree > 180)) {
            time.set(Calendar.AM_PM, (time.get(Calendar.AM_PM) == Calendar.AM ? Calendar.PM : Calendar.AM));
            updateArcColor();
        }
    }

    public void setOnChange(OnSliderRangeMovedListener listener) {
        mListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                int x = (int) ev.getX();
                int y = (int) ev.getY();

                int mThumbSize = getStartThumbSize();
                boolean isThumbStartPressed = x < mThumbStartX + mThumbSize
                        && x > mThumbStartX - mThumbSize
                        && y < mThumbStartY + mThumbSize
                        && y > mThumbStartY - mThumbSize;

                mThumbSize = getEndThumbSize();
                boolean isThumbEndPressed = x < mThumbEndX + mThumbSize
                        && x > mThumbEndX - mThumbSize
                        && y < mThumbEndY + mThumbSize
                        && y > mThumbEndY - mThumbSize;

                if (isThumbStartPressed) {
                    mIsThumbSelected = true;
                    updateSliderState(x, y, Thumb.START);
                    invalidate();
                    return true;
                } else if (isThumbEndPressed) {
                    mIsThumbEndSelected = true;
                    updateSliderState(x, y, Thumb.END);
                    invalidate();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_MOVE: {
                if (mIsThumbSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    updateSliderState(x, y, Thumb.START);
                    invalidate();
                    return true;
                } else if (mIsThumbEndSelected) {
                    int x = (int) ev.getX();
                    int y = (int) ev.getY();
                    updateSliderState(x, y, Thumb.END);
                    invalidate();
                    return true;
                }
                break;
            }

            case MotionEvent.ACTION_UP: {
                mIsThumbSelected = false;
                mIsThumbEndSelected = false;
                break;
            }
        }
        return false;
    }

    enum Thumb {
        START, END
    }

    public interface OnSliderRangeMovedListener {
        void onChange(Calendar start, Calendar end);
    }
}
