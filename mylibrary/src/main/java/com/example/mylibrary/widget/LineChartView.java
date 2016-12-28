package com.example.mylibrary.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;


import com.example.mylibrary.R;
import com.example.mylibrary.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hyyx on 16/12/5.
 * 周或月的折线图
 */

public class LineChartView extends View {

    private int mWidth;
    private int mHeight;
    private Paint mYlinePaint;
    private int axisDivideSizeY = 7;
    private int axisDivideSizeX = 5;

    private final int originX = 0; //坐标原点位置
    private final int originY = 0; //坐标原点位置
    private Paint mTextPaint;
    private float cellHeight;
    private float cellWidth;
    private List<String> pointData = new ArrayList<>();
    private float maxValue = 120;

    private float xAddedNum = CommonUtils.Dp2Px(getContext(), 80);//每次在x轴移动的距离
    private boolean isDrawFirst;//是否是第一次绘制
    private Paint mDataLinePaint;//画折线的


    private float firstX;
    private float firstY;

    private float startX = CommonUtils.Dp2Px(getContext(), 20);
    private float startY = 0;

    private Path path;
    private Paint mConverBgColorPaint;//用于画折线图下面的渐变

    private Paint mFilledCirclePaint;//用于画实心圆
    private Paint mCirclePaint;//用于画空心圆
    private int chartMarginHorizontal = CommonUtils.Dp2Px(getContext(), 10);//折线图距离父控件的距离
    private int valueAlignBottom = CommonUtils.Dp2Px(getContext(), 10);//数据离点的距离
    private float circleFilledRadius = CommonUtils.Dp2Px(getContext(), 3);//外圆半径
    private float alignCircleFilledRadius = CommonUtils.Dp2Px(getContext(), 2);//外圆半径

    private float circleRadius = CommonUtils.Dp2Px(getContext(), 2.5f);//内圆半径


    public static final int BloodSugar_LineChart = 0x0001;
    public static final int BloodPressure_LineChart = 0x0010;


    public static final int DateType_Week = 0x2000;
    public static final int DateType_Mouth = 0x2001;

    private int[] yLabels;
    private Paint mDateLineHightPaint;
    private Paint mDateLineLowPaint;
    private int mCharType;
    private int mDateType;
    private Path pathLow;

    public LineChartView(Context context) {
        this(context, null, 0);
    }


    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mYlinePaint = new Paint();
        mTextPaint = new Paint();
        mDataLinePaint = new Paint();
        path = new Path();
        pathLow = new Path();
        cellWidth = mWidth / axisDivideSizeX;
        cellHeight = mHeight / axisDivideSizeY;
        mConverBgColorPaint = new Paint();
        mFilledCirclePaint = new Paint();
        mCirclePaint = new Paint();
        mDateLineHightPaint = new Paint();
        mDateLineLowPaint = new Paint();
        initPaint();

    }

    //默认paint设置
    private void initPaint() {
        mDataLinePaint.setStyle(Paint.Style.STROKE);
        mDataLinePaint.setStrokeWidth(CommonUtils.Dp2Px(getContext(), 1));
        mDataLinePaint.setAntiAlias(true);

        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setAntiAlias(true);

        mFilledCirclePaint.setStyle(Paint.Style.STROKE);
        mFilledCirclePaint.setAntiAlias(true);
        mFilledCirclePaint.setStrokeWidth(3);

        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(CommonUtils.Dp2Px(getContext(), 10));

        mYlinePaint.setStyle(Paint.Style.STROKE);
        mYlinePaint.setColor(getContext().getResources().getColor(R.color.gray_color_d8));

        mDateLineHightPaint.setColor(getContext().getResources().getColor(R.color.text_red_color));
        mDateLineHightPaint.setStyle(Paint.Style.STROKE);
        mDateLineHightPaint.setStrokeWidth(CommonUtils.Dp2Px(getContext(), 1));
        mDateLineHightPaint.setAntiAlias(true);

        mDateLineLowPaint.setColor(getContext().getResources().getColor(R.color.light_blue));
        mDateLineLowPaint.setStyle(Paint.Style.STROKE);
        mDateLineLowPaint.setStrokeWidth(CommonUtils.Dp2Px(getContext(), 1));
        mDateLineLowPaint.setAntiAlias(true);

    }

    public void setPointdata(List<String> pointData) {
        if (pointData != null) {
            this.pointData.clear();
            this.pointData.addAll(pointData);
            // TODO: 16/12/6 对数据进行处理，获取最大值
            invalidate();
        }

    }

    public void setLineCharType(int type) {
        mCharType = type;
        invalidate();
    }

    public void setDateType(int type) {
        mDateType = type;
        invalidate();


    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);


        if (pointData != null) {
            // mWidth = (pointData.size() - 1) * xAddedNum + (2 * chartMarginHorizontal);
            mWidth = 6 * CommonUtils.Dp2Px(getContext(), 80) + (2 * chartMarginHorizontal);
            mHeight = heightSize;
            setMeasuredDimension(mWidth, mHeight);
        } else {

            if (widthMode == MeasureSpec.EXACTLY) {
                mWidth = widthSize;
            } else if (widthMode == MeasureSpec.AT_MOST) {
                throw new IllegalArgumentException("width must be EXACTLY,you should set like android:width=\"200dp\"");
            }


            if (heightMode == MeasureSpec.EXACTLY) {
                mHeight = heightSize;
            } else if (widthMeasureSpec == MeasureSpec.AT_MOST) {

                throw new IllegalArgumentException("height must be EXACTLY,you should set like android:height=\"200dp\"");
            }

            setMeasuredDimension(mWidth, mHeight);


        }


    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init();
        isDrawFirst = true;
        switch (mCharType) {
            case BloodPressure_LineChart:
                drawDoubleLineChar(canvas);
                break;
            case BloodSugar_LineChart:
                drawLineChar(canvas);
                break;
        }
        drawYline(canvas);
        drawYText(canvas);
    }

    private void drawDoubleLineChar(Canvas canvas) {

        switch (mDateType) {
            case DateType_Week:
                xAddedNum = CommonUtils.Dp2Px(getContext(), 80);
                break;
            case DateType_Mouth:
                xAddedNum = (mWidth - 2 * chartMarginHorizontal) / 4 / 5;
                break;
        }

        float mLineChartHight = (originY + cellHeight * (5 + 1));
        for (int i = 0; i < pointData.size(); i++) {
            float value = ((float) Integer.parseInt(pointData.get(i)));
            float valueTow = ((float) Integer.parseInt(pointData.get(i))) - 10;
            startY = mLineChartHight;
            if (isDrawFirst) {
                firstX = startX;
                firstY = startY - (value / maxValue) * mLineChartHight;
                path.moveTo(startX, startY);
                path.lineTo(firstX, firstY);


                firstY = startY - (valueTow / maxValue) * mLineChartHight;
                pathLow.moveTo(startX, startY);
                pathLow.lineTo(firstX, firstY);
                isDrawFirst = false;
            }

            if (i == pointData.size() - 1) {
                path.lineTo(startX, startY - (value / maxValue) * mLineChartHight);
                path.lineTo(startX, startY);
                pathLow.lineTo(startX, startY - (valueTow / maxValue) * mLineChartHight);
                pathLow.lineTo(startX, startY);

            } else {
                path.lineTo(startX, startY - (value / maxValue) * mLineChartHight);
                pathLow.lineTo(startX, startY - (valueTow / maxValue) * mLineChartHight);
                startX += xAddedNum - chartMarginHorizontal;

            }


        }
        canvas.drawPath(path, mDateLineHightPaint);
        canvas.drawPath(pathLow, mDateLineLowPaint);


        startX = CommonUtils.Dp2Px(getContext(), 20);


        path.lineTo(startX + mWidth - chartMarginHorizontal, mLineChartHight);
        path.lineTo(startX, mLineChartHight);
        path.lineTo(firstX, firstY);
        Shader mShaderOne = new LinearGradient(0, 0, 0, mHeight,
                new int[]{Color.parseColor("#FFF0F0"), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        mConverBgColorPaint.setShader(mShaderOne);
        canvas.drawPath(path, mConverBgColorPaint);


        pathLow.lineTo(startX + mWidth - chartMarginHorizontal, mLineChartHight);
        pathLow.lineTo(startX, mLineChartHight);
        pathLow.lineTo(firstX, firstY);
        Shader mShader = new LinearGradient(0, 0, 0, mHeight,
                new int[]{Color.parseColor("#F1F3F9"), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        mConverBgColorPaint.setShader(mShader);
        canvas.drawPath(pathLow, mConverBgColorPaint);

        mCirclePaint.setColor(getContext().getResources().getColor(R.color.white));
        mFilledCirclePaint.setStrokeWidth(3);

        for (int i = 0; i < pointData.size(); i++) {
            float value = ((float) Integer.parseInt(pointData.get(i)));
            float valueTow = ((float) Integer.parseInt(pointData.get(i))) - 10;

            mTextPaint.setColor(getContext().getResources().getColor(R.color.text_red_color));
            mCirclePaint.setColor(getContext().getResources().getColor(R.color.white));
            mFilledCirclePaint.setColor(getContext().getResources().getColor(R.color.text_red_color));
            canvas.drawCircle(startX, startY - (value / maxValue) * mLineChartHight, circleFilledRadius, mFilledCirclePaint);
            canvas.drawCircle(startX, startY - (value / maxValue) * mLineChartHight, circleRadius, mCirclePaint);
            mTextPaint.setColor(getContext().getResources().getColor(R.color.text_red_color));
            //canvas.drawText(value + "", startX, startY - (value / maxValue) * mLineChartHight - valueAlignBottom, mTextPaint);


            mTextPaint.setColor(getContext().getResources().getColor(R.color.light_blue));
            mCirclePaint.setColor(getContext().getResources().getColor(R.color.white));
            mFilledCirclePaint.setColor(getContext().getResources().getColor(R.color.light_blue));
            canvas.drawCircle(startX, startY - (valueTow / maxValue) * mLineChartHight, circleFilledRadius, mFilledCirclePaint);
            canvas.drawCircle(startX, startY - (valueTow / maxValue) * mLineChartHight, circleRadius, mCirclePaint);
            mTextPaint.setColor(getContext().getResources().getColor(R.color.light_blue));
            // canvas.drawText(valueTow + "", startX, startY - (valueTow / maxValue) * mLineChartHight - valueAlignBottom, mTextPaint);


            mTextPaint.setColor(getContext().getResources().getColor(R.color.text_light_color));
            // canvas.drawText("5-3", startX, mLineChartHight + cellHeight / 3, mTextPaint);
            switch (mDateType) {
                case DateType_Week:
                    canvas.drawText("5-3", startX, mLineChartHight + cellHeight / 3, mTextPaint);
                    break;
                case DateType_Mouth:
                    if (i == 0) {
                        canvas.drawText(1 + "", startX, mLineChartHight + cellHeight / 3, mTextPaint);
                    } else {
                        if ((i + 1) % 7 == 0) {
                            canvas.drawText(i + 1 + "", startX, mLineChartHight + cellHeight / 3, mTextPaint);
                        }
                    }


                    break;
            }
            startX += xAddedNum - chartMarginHorizontal;


        }

        path.reset();
        pathLow.reset();
        startX = CommonUtils.Dp2Px(getContext(), 20);


    }

    private void drawLineChar(Canvas canvas) {
        mDataLinePaint.setColor(getContext().getResources().getColor(R.color.titlecolor));
        switch (mDateType) {
            case DateType_Week:
                xAddedNum = CommonUtils.Dp2Px(getContext(), 80);
                break;
            case DateType_Mouth:
                xAddedNum = (mWidth - 2 * chartMarginHorizontal) / 4 / 5;
                break;
        }

        float mLineChartHight = (originY + cellHeight * (5 + 1));
        for (int i = 0; i < pointData.size(); i++) {
            float value = ((float) Integer.parseInt(pointData.get(i)));
            startY = mLineChartHight;

            if (isDrawFirst) {
                firstX = startX;
                firstY = startY - (value / maxValue) * mLineChartHight;
                path.moveTo(startX, startY);
                path.lineTo(firstX, firstY + circleFilledRadius);
                isDrawFirst = false;

            }

            if (i == pointData.size() - 1) {
                path.lineTo(startX, startY - (value / maxValue) * mLineChartHight);
                path.lineTo(startX, startY);

            } else {
                path.lineTo(startX, startY - (value / maxValue) * mLineChartHight);
                startX += xAddedNum - chartMarginHorizontal;

            }
        }
        path.lineTo(startX, startY);
        canvas.drawPath(path, mDataLinePaint);
        startX = CommonUtils.Dp2Px(getContext(), 20);
        path.lineTo(startX + mWidth - chartMarginHorizontal, mLineChartHight);
        path.lineTo(startX, mLineChartHight);
        path.lineTo(firstX, firstY);

        // 新建一个线性渐变，前两个参数是渐变开始的点坐标，第三四个参数是渐变结束的点的坐标。连接这2个点就拉出一条渐变线了，
        // 数组是渐变的颜色。下一个参数是渐变颜色的分布，
        // 如果为空，每个颜色就是均匀分布的。最后是模式，这里设置的是循环渐变
        Shader mShader = new LinearGradient(0, 0, 0, mHeight,
                new int[]{Color.parseColor("#EBFCFB"), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        mConverBgColorPaint.setShader(mShader);
        canvas.drawPath(path, mConverBgColorPaint);

        mCirclePaint.setColor(getContext().getResources().getColor(R.color.white));
        mFilledCirclePaint.setColor(getContext().getResources().getColor(R.color.titlecolor));

        // startY = mLineChartHight;

        //List<LineChartPointBean> pointlist = new ArrayList<>();

        for (int i = 0; i < pointData.size(); i++) {
            float value = ((float) Integer.parseInt(pointData.get(i)));
            canvas.drawCircle(startX, startY - (value / maxValue) * mLineChartHight, circleFilledRadius, mFilledCirclePaint);
            // pointlist.add(new LineChartPointBean(startX, startY - (value / maxValue) * mLineChartHight));
            canvas.drawCircle(startX, startY - (value / maxValue) * mLineChartHight, circleRadius, mCirclePaint);
            mTextPaint.setColor(getContext().getResources().getColor(R.color.titlecolor));
            // canvas.drawText(value + "", startX, startY - (value / maxValue) * mLineChartHight - valueAlignBottom, mTextPaint);
            mTextPaint.setColor(getContext().getResources().getColor(R.color.text_light_color));
            //canvas.drawText("5-3", startX, mLineChartHight + cellHeight / 3, mTextPaint);

            switch (mDateType) {
                case DateType_Week:
                    canvas.drawText("5-3", startX, mLineChartHight + cellHeight / 3, mTextPaint);
                    break;
                case DateType_Mouth:
                    if (i == 0) {
                        canvas.drawText(1 + "", startX, mLineChartHight + cellHeight / 3, mTextPaint);
                    } else {
                        if ((i + 1) % 7 == 0) {
                            canvas.drawText(i + 1 + "", startX, mLineChartHight + cellHeight / 3, mTextPaint);
                        }
                    }


                    break;
            }
            startX += xAddedNum - chartMarginHorizontal;

        }


        path.reset();
        startX = CommonUtils.Dp2Px(getContext(), 20);
//        for (int i = 0; i < pointData.size(); i++) {
//
//            if (i == 0) {
//                // canvas.drawLine(startX, startY, pointlist.get(0).getX(), pointlist.get(0).getY() + circleFilledRadius, mDataLinePaint);
//                path.moveTo(startX, startY);
//                path.lineTo(pointlist.get(0).getX(), pointlist.get(0).getY() + circleFilledRadius);
//
//            }
//            if (i != pointData.size() - 1) {
//                if (((float) Integer.parseInt(pointData.get(i))) > ((float) Integer.parseInt(pointData.get(i + 1)))) {
//                    //   canvas.drawLine(pointlist.get(i).getX(), pointlist.get(i).getY() + alignCircleFilledRadius, pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY() - alignCircleFilledRadius, mDataLinePaint);
//                    // path.moveTo(pointlist.get(i).getX(), pointlist.get(i).getY() + alignCircleFilledRadius);
//                    //  path.lineTo(pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY() - alignCircleFilledRadius);
//
//                    path.quadTo(pointlist.get(i).getX(), pointlist.get(i).getY() + alignCircleFilledRadius, pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY() - alignCircleFilledRadius);
//                }
//                if (((float) Integer.parseInt(pointData.get(i))) < ((float) Integer.parseInt(pointData.get(i + 1)))) {
//                    //   canvas.drawLine(pointlist.get(i).getX(), pointlist.get(i).getY() - alignCircleFilledRadius, pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY() + alignCircleFilledRadius, mDataLinePaint);
//                    path.quadTo(pointlist.get(i).getX(), pointlist.get(i).getY() - alignCircleFilledRadius, pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY() + alignCircleFilledRadius);
//
//                    // path.moveTo(pointlist.get(i).getX(), pointlist.get(i).getY() - alignCircleFilledRadius);
//                    //path.lineTo(pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY() + alignCircleFilledRadius);
//
//
//                }
//                if (((float) Integer.parseInt(pointData.get(i))) == ((float) Integer.parseInt(pointData.get(i + 1)))) {
//                    // canvas.drawLine(pointlist.get(i).getX() + alignCircleFilledRadius, pointlist.get(i).getY(), pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY(), mDataLinePaint);
//                    path.quadTo(pointlist.get(i).getX() + alignCircleFilledRadius, pointlist.get(i).getY(), pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY());
//
//                    // path.moveTo(pointlist.get(i).getX() + alignCircleFilledRadius, pointlist.get(i).getY());
//                    // path.lineTo(pointlist.get(i + 1).getX() - alignCircleFilledRadius, pointlist.get(i + 1).getY());
//
//
//                }
//
//
//            }
//            if (i == pointData.size() - 1) {
//                // canvas.drawLine(pointlist.get(i).getX(), pointlist.get(i).getY() + alignCircleFilledRadius, pointlist.get(i).getX(), startY, mDataLinePaint);
//                path.quadTo(pointlist.get(i).getX(), pointlist.get(i).getY() + alignCircleFilledRadius, pointlist.get(i).getX(), startY);
//
//                // path.moveTo(pointlist.get(i).getX(), pointlist.get(i).getY() + alignCircleFilledRadius);
//                // path.lineTo(pointlist.get(i).getX(), startY);
//
//            }
//
//
//        }
//
//        //  mDateLineLowPaint.setColor(getContext().getResources().getColor(R.color.line_red));
//        // canvas.drawPath(path, mDataLinePaint);


    }


    private void drawYText(Canvas canvas) {
        mTextPaint.setColor(getContext().getResources().getColor(R.color.text_light_color));
        int i1 = ((int) maxValue) / (axisDivideSizeY - 1);
        for (int i = 0; i < axisDivideSizeY - 1; i++) {
            canvas.drawText(i1 * (axisDivideSizeY - 2 - i) + "", originX + mWidth + getScrollX() - 50, (originY + cellHeight * (i + 1) - 20), mTextPaint);
        }
    }

    private void drawYline(Canvas canvas) {
        for (int i = 0; i < axisDivideSizeY; i++) {
            if (i == axisDivideSizeY - 1) {
                canvas.drawLine(originX + chartMarginHorizontal, (originY + cellHeight * (i + 1)) - cellHeight / 2, originX + mWidth - chartMarginHorizontal, (originY + cellHeight * (i + 1)) - cellHeight / 2, mYlinePaint);
            } else if (i == axisDivideSizeY - 2) {
                canvas.drawLine(originX + chartMarginHorizontal, (originY + cellHeight * (i + 1)), originX + mWidth - chartMarginHorizontal, (originY + cellHeight * (i + 1)), mYlinePaint);
            } else {
                Path path = new Path();
                path.moveTo(originX + chartMarginHorizontal, (originY + cellHeight * (i + 1)));
                path.lineTo(originX + mWidth - chartMarginHorizontal, (originY + cellHeight * (i + 1)));
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                mYlinePaint.setPathEffect(effects);
                canvas.drawPath(path, mYlinePaint);
            }


        }


    }
}
