package com.example.hyyx.hyyxshape.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


import com.example.hyyx.hyyxshape.bean.PieData;
import com.example.hyyx.hyyxshape.utils.CommonUtils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by hyyx on 2016/12/9.
 * 空心圆
 */

public class HollowPieCharView extends View {
    private static final int DEFAULT_MINANGLE = 30;
    private List<PieData> mPieData = new ArrayList<>();
    private Paint mPaint;//饼状画笔
    private Paint mTextPaint; // 文字画笔
    private int DEFAULT_RADIUS = CommonUtils.Dp2Px(getContext(), 150);
    private int mRadius = DEFAULT_RADIUS; //外圆的半径
    private float animatedValue;
    private RectF oval;
    private RectF touchOval;
    private String centerTitle;
    private int width, height;
    private int mUseWidth, mUseHeight;
    private float mTotalValue;
    private float[] pieAngles;
    private int position = -1;
    private Paint mLinePaint;
    private DecimalFormat dff;
    private boolean isHaveTextLine;





    public HollowPieCharView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HollowPieCharView(Context context) {
        this(context, null, 0);
    }

    public HollowPieCharView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(CommonUtils.Dp2Px(getContext(), 40));
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(24);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        oval = new RectF();
        touchOval = new RectF();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.FILL);
        mLinePaint.setStrokeWidth(2);
        mLinePaint.setColor(Color.BLACK);

        dff = new DecimalFormat("0.0");

    }


    public void setRadius(int mRadius) {
        this.mRadius = mRadius;
    }


    public void setCenterTitle(String centerTitle) {
        this.centerTitle = centerTitle;
    }


    public void setPieData(List<PieData> data) {
        mPieData.clear();
        mPieData.addAll(data);
        mTotalValue = 0;
        for (PieData piedata : mPieData) {
            mTotalValue += piedata.getValue();
        }
        pieAngles = new float[mPieData.size()];

        if (mPieData.size() > 4) {
            isHaveTextLine = false;
        } else {
            isHaveTextLine = true;
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wideMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            width = wideSize;
        } else {
            width = mRadius * 2 + getPaddingLeft() + getPaddingRight();
            if (wideMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, wideSize);
            }

        }

        if (heightMode == MeasureSpec.EXACTLY) { //精确值 或matchParent
            height = heightSize;
        } else {
            height = mRadius * 2 + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }

        }
        setMeasuredDimension(width, height);

    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mUseWidth = w - getPaddingLeft() - getPaddingRight();
        mUseHeight = h - getPaddingTop() - getPaddingBottom();

        mRadius = (int) (Math.min(mUseWidth, mUseHeight) / 2 * 0.5);

        oval.left = -mRadius;
        oval.top = -mRadius;
        oval.right = mRadius;
        oval.bottom = mRadius;

        touchOval.left = -mRadius - 16;
        touchOval.top = -mRadius - 16;
        touchOval.right = mRadius + 16;
        touchOval.bottom = mRadius + 16;
    }


    @Override
    protected void onDraw(Canvas mCanvas) {
        super.onDraw(mCanvas);

        mCanvas.translate((getWidth() + getPaddingLeft() - getPaddingRight()) / 2, (getHeight() + getPaddingTop() - getPaddingBottom()) / 2);
        drawPie(mCanvas);
        drawCenterText(mCanvas, centerTitle, 0, 0, mTextPaint);

    }


    private void drawPie(Canvas canvas) {
        float currentAngle = 0.0f;
        for (int i = 0; i < mPieData.size(); i++) {
            float needDrawAngle = mPieData.get(i).getValue() * 1.0f / mTotalValue * 360;
            mPaint.setColor(mPieData.get(i).getColor());
            if (Math.min(needDrawAngle, animatedValue - currentAngle) >= 0) {

                if (position - 1 == i) {
                    canvas.drawArc(touchOval, currentAngle, Math.min(needDrawAngle, animatedValue - currentAngle), false, mPaint);
                   if (isHaveTextLine){
                       drawTextLine(canvas, currentAngle, needDrawAngle);
                   }

                } else {
                    canvas.drawArc(oval, currentAngle, Math.min(needDrawAngle, animatedValue - currentAngle), false, mPaint);
                    if (isHaveTextLine){
                        drawTextLine(canvas, currentAngle, needDrawAngle);
                    }


                }

            }

            pieAngles[i] = currentAngle;
            currentAngle += needDrawAngle;


            float res = mPieData.get(i).getValue() / mTotalValue * 100;
            String resToRound = dff.format(res);
//            if (currentAngle % 360.0 >= 90.0 && currentAngle % 360.0 <= 270.0) {
//                canvas.drawText(resToRound + "%", pxt - mTextPaint.measureText(resToRound + "%"), pyt, mTextPaint);
//            } else {
//                canvas.drawText(resToRound + "%", pxt, pyt, mTextPaint);
//            }

        }


    }

    private void drawTextLine(Canvas canvas, float currentAngle, float needDrawAngle) {
        float pxs = (float) ((mRadius + CommonUtils.Dp2Px(getContext(), 20)) * Math.cos(Math.toRadians(currentAngle + needDrawAngle / 2)));
        float pys = (float) ((mRadius + CommonUtils.Dp2Px(getContext(), 20)) * Math.sin(Math.toRadians(currentAngle + needDrawAngle / 2)));
        float pxt = (float) (((mRadius + CommonUtils.Dp2Px(getContext(), 20)) + 50) * Math.cos(Math.toRadians(currentAngle + needDrawAngle / 2)));
        float pyt = (float) (((mRadius + CommonUtils.Dp2Px(getContext(), 20)) + 50) * Math.sin(Math.toRadians(currentAngle + needDrawAngle / 2)));

        float textAngle = currentAngle + needDrawAngle / 2 + 90;


        canvas.drawLine(pxs, pys, pxt, pyt, mLinePaint);

        if (textAngle > 360) {
            textAngle = textAngle - 360;
        }

        if (textAngle >= 0 && textAngle <= 180) { //画布坐标系第一象限(数学坐标系第四象限)
            mTextPaint.setTextAlign(Paint.Align.LEFT);
            String content = "测试文字123";
            float v = mTextPaint.measureText(content);
            canvas.drawLine(pxt, pyt, pxt + v + 20, pyt, mLinePaint);
            canvas.drawText(content, pxt, pyt - 20, mTextPaint);
        } else if (textAngle > 180 && textAngle <= 360) { //画布坐标系第三象限(数学坐标系第二象限)
            mTextPaint.setTextAlign(Paint.Align.RIGHT);
            String content = "测试文字456789";
            float v = mTextPaint.measureText(content);
            canvas.drawLine(pxt, pyt, pxt - v - 20, pyt, mLinePaint);
            canvas.drawText(content, pxt, pyt - 20, mTextPaint);

        }
    }


    //画中间文字标题
    private void drawCenterText(Canvas mCanvas, String text, float x, float y, Paint mPaint) {
        Rect rect = new Rect();
        mTextPaint.setTextSize(20);
        mTextPaint.getTextBounds(text, 0, text.length(), rect);
        mCanvas.drawText(text, x, y + rect.height() / 2, mTextPaint);
    }


    public void startDraw() {
        if (mPieData != null && centerTitle != null) {
            initAnimator();
        }
    }

    private void initAnimator() {
        ValueAnimator anim = ValueAnimator.ofFloat(0, 360);
        anim.setDuration(10000);
        anim.setInterpolator(new AccelerateDecelerateInterpolator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                animatedValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        anim.start();
    }


    public boolean onTouchEvent(MotionEvent event) {
        if (isHaveTextLine){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float x = event.getX() - (mUseWidth / 2);
                    float y = event.getY() - (mUseHeight / 2);
                    float touchAngle = 0;
                    if (x < 0 && y < 0) {  //2 象限
                        touchAngle += 180;
                    } else if (y < 0 && x > 0) {  //1象限
                        touchAngle += 360;
                    } else if (y > 0 && x < 0) {  //3象限
                        touchAngle += 180;
                    }
                    //Math.atan(y/x) 返回正数值表示相对于 x 轴的逆时针转角，返回负数值则表示顺时针转角。
                    //返回值乘以 180/π，将弧度转换为角度。
                    touchAngle += Math.toDegrees(Math.atan(y / x));
                    if (touchAngle < 0) {
                        touchAngle = touchAngle + 360;
                    }
                    float touchRadius = (float) Math.sqrt(y * y + x * x);
                    if (touchRadius < mRadius) {
                        position = -Arrays.binarySearch(pieAngles, (touchAngle)) - 1;
                        invalidate();

                    }
                    break;
            }


        }
        return super.onTouchEvent(event);
    }
}

