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
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;


import com.example.mylibrary.R;
import com.example.mylibrary.bean.NuringInfoShapeBean;
import com.example.mylibrary.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by ${hyyx} on 2016/6/30 0030.
 * 医院体征单使用的几种自定义图形
 */
public class iHospitalShapeView extends View {


    private Paint mPaint;
    private Paint paintDotted;
    private Paint paintRedLine;
    private Paint painPoint;
    private Paint paintShader;
    private Paint paintAxisScaleMark;
    private Paint paintText;


    //X坐标轴最大值
    private float maxAxisValueX = 900;
    //X坐标轴刻度线数量
    private int axisDivideSizeX = 7;
    //Y坐标轴最大值
    private float maxAxisValueY = 700;
    //Y坐标轴刻度线数量
    private int axisDivideSizeY = 20;
    private int width;
    private int height;
    private final int originX = CommonUtils.Dp2Px(getContext(), 20);
    private final int originY = 10;


    public static final int TYPE_TEMP = 1;
    public static final int TYPE_BLOODPRESURE = 2;
    public static final int TYPE_PULSE = 3;
    public static final int TYPE_BLOODSUGAR = 4;
    public static final int TYPE_BREATHE = 5;

    private int DRAWSHAPE = 0;
    public static final int DRAWColumn = 99;
    public static final int DRAWBROKRNLINE = 98;

    private String[] xLabels;
    private int[] yLabels = new int[20];
    private float reddataOne, reddataTwo;
    private Path path;
    private float mShapeHeight;
    private List<NuringInfoShapeBean> nuringInfoShapeBeen = new ArrayList<>();
    private boolean isSingleLine = true;
    private boolean isPoint = false;
    private boolean isDrawMarkYLine = true;
    private boolean isDottedY = false;
    private boolean isDrawMarkXLine = false;
    private boolean isDottedX = false;


    public iHospitalShapeView(Context context) {
        this(context, null, 0);
    }

    public iHospitalShapeView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public iHospitalShapeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {

        mPaint = new Paint();
        paintDotted = new Paint();
        paintRedLine = new Paint();
        painPoint = new Paint();
        paintShader = new Paint();
        paintAxisScaleMark = new Paint();
        paintText = new Paint();
        paintRedLine = new Paint();

        initPaint();


    }

    private void initPaint() {
        mPaint.setColor(getContext().getResources().getColor(R.color.titlecolor));
        mPaint.setStrokeWidth(2);
        mPaint.setAntiAlias(true);


        paintAxisScaleMark.setColor(Color.BLACK);
        paintAxisScaleMark.setStrokeWidth(2);
        paintAxisScaleMark.setAntiAlias(true);


        paintText.setColor(Color.BLACK);
        paintText.setTextSize(CommonUtils.sp2px(getContext(), 10));
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);

        paintDotted.setStyle(Paint.Style.STROKE);


        painPoint.setAntiAlias(true);
        painPoint.setStyle(Paint.Style.STROKE);

        paintRedLine.setStyle(Paint.Style.STROKE);
        paintRedLine.setStrokeWidth(2);
        paintRedLine.setColor(Color.RED);


    }


    /**
     * 设置X轴的最大值及刻度线数量（包括0坐标刻度）
     *
     * @param maxValue   X轴的最大值
     * @param divideSize 刻度线数量
     */
    public void setAxisX(float maxValue, int divideSize) {
        maxAxisValueX = maxValue;
        axisDivideSizeX = divideSize;
        invalidate();
    }

    /**
     * 设置Y轴的最大值及刻度线数量（包括0坐标刻度）
     *
     * @param maxValue   Y轴的最大值
     * @param divideSize 刻度线数量
     */
    public void setAxisY(float maxValue, int divideSize) {
        maxAxisValueY = maxValue;
        axisDivideSizeY = divideSize;
        invalidate();
    }


    /**
     * 设置X轴的数值
     *
     * @param xLabels X轴上各点的数据
     */


    public void setXdataInfo(String[] xLabels) {
        this.xLabels = xLabels;
        invalidate();
    }


    /**
     * 设置变的Y轴数据
     *
     * @param yLabels
     */
    public void setChangeableYdataInfo(int[] yLabels) {
        this.yLabels = yLabels;
        invalidate();
    }

    /**
     * 设置view自带的Y轴数据,包括体温,血压,脉搏,血糖,呼吸的五种标准化数值
     *
     * @param type 5种type类型规定不同的标准化数值
     */


    public void setDefaultYdataInfo(int type) {
        switch (type) {
            case TYPE_TEMP:
                yLabels = new int[]{32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43};
                setAxisY(43, 12);
                break;

            case TYPE_BLOODPRESURE:
                yLabels = new int[]{40, 60, 80, 100, 120, 140, 160, 180, 200};
                setAxisY(200, 9);
                break;
            case TYPE_PULSE:
                yLabels = new int[]{20, 40, 60, 80, 100, 120, 140};
                setAxisY(140, 7);
                break;
            case TYPE_BLOODSUGAR:
                yLabels = new int[]{0, 3, 6, 9, 12, 15, 18, 21, 24, 27};
                setAxisY(27, 10);
                break;
            case TYPE_BREATHE:
                yLabels = new int[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50};
                setAxisY(50, 10);
                break;
        }
        invalidate();


    }


    public void setShapeInfo(List<NuringInfoShapeBean> nuringInfoShapeBeen, int drawShape) {
        this.nuringInfoShapeBeen.clear();
        this.nuringInfoShapeBeen.addAll(nuringInfoShapeBeen);
        DRAWSHAPE = drawShape;
        invalidate();
    }

    public void isPoint(boolean isPoint) {
        this.isPoint = isPoint;
        invalidate();
    }


    public void setBloodpresureShape(boolean isSinleling, boolean isPoint) {
        this.isSingleLine = isSinleling;
        this.isPoint = isPoint;
        invalidate();
    }


    /**
     * 设置红线
     *
     * @param reddataOne 红线1
     * @param reddataTwo 红线2
     */

    public void setRedLine(float reddataOne, float reddataTwo) {
        this.reddataOne = reddataOne;
        this.reddataTwo = reddataTwo;
        invalidate();

    }


    public void isDrawMarkY(boolean isDrawMarkYLine, boolean isDottedY) {
        this.isDrawMarkYLine = isDrawMarkYLine;
        this.isDottedY = isDottedY;


    }

    public void isDrawMarkY(boolean isDrawMarkYLine) {
        this.isDrawMarkYLine = isDrawMarkYLine;

    }


    public void isDrawMarkX(boolean isDrawMarkXLine, boolean isDottedX) {
        this.isDrawMarkXLine = isDrawMarkXLine;
        this.isDottedX = isDottedX;


    }

    public void isDrawMarkX(boolean isDrawMarkXLine) {
        this.isDrawMarkXLine = isDrawMarkXLine;

    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int wideSize = MeasureSpec.getSize(widthMeasureSpec);
        int wideMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wideMode == MeasureSpec.EXACTLY) {
            width = wideSize;
        }


        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        }
        setMeasuredDimension(width, height);


    }


    @Override
    public void onDraw(Canvas canvas) {

        mShapeHeight = height - CommonUtils.Dp2Px(getContext(), 25);
        drawAxisXandY(canvas);//X,Y轴
        if (isDrawMarkYLine) {
            drawMarkY(canvas, isDottedY);//Y轴各点延长线，可选实线，虚线
        }
        if (isDrawMarkXLine) {
            drawMarkX(canvas, isDottedX);//X轴各点延长线，可选实线，虚线
        }


        //drawAxisScaleMarkXandY(canvas);//XY轴各点的刻度线
        drawAxisScaleMarkValueX(canvas, mPaint);//x轴的值
        drawAxisScaleMarkValueY(canvas, mPaint);//y轴的值
        switch (DRAWSHAPE) {
            case DRAWColumn:
                if (reddataOne != 0 && reddataTwo != 0) {
                    drawShader(canvas, paintShader, reddataTwo, reddataOne, "#B3F5F2", getContext().getResources().getColor(R.color.titlecolor));//阴影
                } else {
                    if (reddataOne + reddataTwo != 0) {
                        drawRedLineMarkY(canvas, reddataTwo);
                    }

                }

                drawColumn(canvas, mPaint);

                break;
            case DRAWBROKRNLINE:
                if (isSingleLine) {
                    if (reddataOne != 0 && reddataTwo != 0) {
                        drawShader(canvas, paintShader, reddataTwo, reddataOne, "#B3F5F2", getContext().getResources().getColor(R.color.titlecolor));//阴影
                    } else {
                        if (reddataOne + reddataTwo != 0) {
                            drawRedLineMarkY(canvas, reddataTwo);
                        }

                    }
                } else {
                    drawShader(canvas, paintShader, 90, 60, "#B3F5F2", getContext().getResources().getColor(R.color.titlecolor));
                    drawShader(canvas, paintShader, 140, 90, "#CDE2F7", getContext().getResources().getColor(R.color.light_blue));
                }

                drawLine(canvas, isPoint, isSingleLine);//绘制高低压,可选散点或者折线图
                break;

        }
    }


    private void drawAxisXandY(Canvas canvas) {
        mPaint.setColor(getContext().getResources().getColor(R.color.titlecolor));
        canvas.drawLine(originX, originY + mShapeHeight, originX + width, originY + mShapeHeight, mPaint);
        canvas.drawLine(originX, originY, originX, originY + mShapeHeight, mPaint);
    }

    private void drawAxisScaleMarkXandY(Canvas canvas) {
        float cellWidth = width / axisDivideSizeX;
        for (int i = 0; i < axisDivideSizeX - 1; i++) {
            canvas.drawLine(cellWidth * (i + 1) + originX, originY + mShapeHeight - 10, cellWidth * (i + 1) + originX, originY + mShapeHeight, paintAxisScaleMark);
        }

        float cellHeight = mShapeHeight / axisDivideSizeY;
        for (int i = 0; i < axisDivideSizeY - 1; i++) {
            canvas.drawLine(originX, (originY + cellHeight * (i + 1)), originX + 10, (originY + cellHeight * (i + 1)), paintAxisScaleMark);
        }


    }


    private void drawMarkY(Canvas canvas, boolean isDotted) {

        paintDotted.setColor(getContext().getResources().getColor(R.color.border_color));
        float cellHeight = mShapeHeight / axisDivideSizeY;
        for (int i = 0; i < axisDivideSizeY - 1; i++) {

            if (isDotted) {
                Path path = new Path();
                path.moveTo(originX, (originY + cellHeight * (i + 1)));
                path.lineTo(originX + width, (originY + cellHeight * (i + 1)));
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                paintDotted.setPathEffect(effects);
                canvas.drawPath(path, paintDotted);

            } else {
                canvas.drawLine(originX, (originY + cellHeight * (i + 1)), originX + width, (originY + cellHeight * (i + 1)), paintDotted);
            }


        }
    }

    private void drawMarkX(Canvas canvas, boolean isDotted) {

        paintDotted.setColor(getContext().getResources().getColor(R.color.border_color));
        float cellWidth = width / axisDivideSizeX;
        for (int i = 0; i < axisDivideSizeY - 1; i++) {

            if (isDotted) {
                Path path = new Path();
                path.moveTo(originX + cellWidth * (i + 1), originY);
                path.lineTo(originX + cellWidth * (i + 1), originY + mShapeHeight);
                PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
                paintDotted.setPathEffect(effects);
                canvas.drawPath(path, paintDotted);

            } else {
                canvas.drawLine(originX + cellWidth * (i + 1), originY, originX + cellWidth * (i + 1), originY + +mShapeHeight, paintDotted);
            }


        }
    }

    private void drawShader(Canvas canvas, Paint paint, float hight, float low, String shaderColor, int paintColor) {
        float cellHeight = mShapeHeight / axisDivideSizeY;
        int i1 = yLabels[yLabels.length - 1] - yLabels[0];
        float v2 = i1 / (cellHeight * (yLabels.length - 1));


        double v = low - yLabels[0];
        double v1 = v / v2;
        float leftTopOneY = (originY + mShapeHeight) - ((float) v1);

        double vTwo = hight - yLabels[0];
        double v1Two = vTwo / v2;
        float leftTopTwoY = (originY + mShapeHeight) - ((float) v1Two);

        Shader mShader = new LinearGradient(0, 0, 0, mShapeHeight,
                new int[]{Color.parseColor(shaderColor), Color.TRANSPARENT}, null, Shader.TileMode.CLAMP);
        paint.setShader(mShader);
        canvas.drawRect(originX, leftTopTwoY, originX + width, leftTopOneY, paint);

        paintDotted.setColor(paintColor);

        Path path = new Path();
        PathEffect effects = new DashPathEffect(new float[]{5, 5, 5, 5}, 1);
        paintDotted.setPathEffect(effects);


        path.moveTo(originX, leftTopOneY);
        path.lineTo(originX + width, leftTopOneY);
        canvas.drawPath(path, paintDotted);
        path.reset();

        path.moveTo(originX, leftTopTwoY);
        path.lineTo(originX + width, leftTopTwoY);
        canvas.drawPath(path, paintDotted);

        path.reset();

        String content = "标准值";
        paintText.setColor(paintColor);
        float contentWidth = paintText.measureText(content);
        canvas.drawText(content, originX + width - contentWidth - 20, leftTopTwoY - 10, paintText);


    }


    private void drawRedLineMarkY(Canvas canvas, float data) {

        float cellHeight = mShapeHeight / (axisDivideSizeY);
        int i1 = yLabels[yLabels.length - 1] - yLabels[0];
        float v2 = i1 / (cellHeight * (yLabels.length - 1));
        float v = data - yLabels[0];
        float v1 = v / v2;
        float leftTopY = (originY + mShapeHeight) - ((float) v1);
        canvas.drawLine(originX, leftTopY, originX + width, leftTopY, paintRedLine);
    }


    private void drawAxisScaleMarkValueX(Canvas canvas, Paint paint) {
        paint.setColor(Color.GRAY);
        paint.setFakeBoldText(true);
        paint.setTextSize(16);
        paint.setTextAlign(Paint.Align.CENTER);
        float cellWidth = width / axisDivideSizeX;
        for (int i = 0; i < nuringInfoShapeBeen.size(); i++) {

            if (!TextUtils.isEmpty(nuringInfoShapeBeen.get(i).getTime())) {
                canvas.drawText(nuringInfoShapeBeen.get(i).getTime().split(" ")[1], (cellWidth * (i) + originX) + cellWidth, originY + mShapeHeight + CommonUtils.Dp2Px(getContext(), 8), paint);
                canvas.drawText(nuringInfoShapeBeen.get(i).getTime().split(" ")[0], (cellWidth * (i) + originX) + cellWidth, originY + mShapeHeight + CommonUtils.Dp2Px(getContext(), 15), paint);

            }
        }

    }


    private void drawAxisScaleMarkValueY(Canvas canvas, Paint paint) {
        float cellHeight = mShapeHeight / axisDivideSizeY;
        paint.setTextAlign(Paint.Align.CENTER);
        for (int i = 1; i < axisDivideSizeY; i++) {
            canvas.drawText(yLabels[i] + "", originX - CommonUtils.Dp2Px(getContext(), 10), (originY + mShapeHeight) - cellHeight * i, paint);
        }
    }

    private void drawLine(Canvas canvas, boolean isPoint, boolean isSingleLine) {

        float cyLow = 0;
        float cy = 0;
        double v = 0;

        painPoint.setColor(getContext().getResources().getColor(R.color.titlecolor));
        painPoint.setStrokeWidth(CommonUtils.Dp2Px(getContext(), 2));
        float cellWidth = width / axisDivideSizeX;
        float cellHeight = mShapeHeight / axisDivideSizeY;
        path = new Path();


        for (int i = 0; i < nuringInfoShapeBeen.size(); i++) {
            float cx = (cellWidth * (i + 1) + originX);
            int i1 = yLabels[yLabels.length - 1] - yLabels[0];
            float v2 = i1 / (cellHeight * (yLabels.length - 1));
            if (isSingleLine) {
                if (TextUtils.isEmpty(nuringInfoShapeBeen.get(i).getValue())) {
                    break;
                } else {
                    v = Float.parseFloat(nuringInfoShapeBeen.get(i).getValue()) - yLabels[0];
                }

            } else {
                if (TextUtils.isEmpty(nuringInfoShapeBeen.get(i).getLowValue())) {
                    break;
                } else {
                    v = Float.parseFloat(nuringInfoShapeBeen.get(i).getLowValue()) - yLabels[0];
                }
            }
            double v1 = v / v2;
            cyLow = (originY + mShapeHeight) - ((float) v1);
            painPoint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx, cyLow, 7, painPoint);


            if (!isPoint) {
                if (i == 0) {
                    path.moveTo(cx, cyLow);
                } else {
                    path.lineTo(cx, cyLow);
                }
            }


        }


        if (!isPoint) {
            painPoint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, painPoint);
            path.reset();
        }


        painPoint.setColor(getContext().getResources().getColor(R.color.light_blue));
        painPoint.setAntiAlias(true);

        for (int i = 0; i < nuringInfoShapeBeen.size(); i++) {
            float cx = (cellWidth * (i + 1) + originX);
            int i1 = yLabels[yLabels.length - 1] - yLabels[0];

            if (TextUtils.isEmpty(nuringInfoShapeBeen.get(i).getHightValue())) {
                break;
            } else {
                float v2 = i1 / (cellHeight * (yLabels.length - 1));
                double vv = Float.parseFloat(nuringInfoShapeBeen.get(i).getHightValue()) - yLabels[0];
                double v1 = vv / v2;
                cy = (originY + mShapeHeight) - ((float) v1);
                painPoint.setStyle(Paint.Style.FILL);
                canvas.drawCircle(cx, cy, 7, painPoint);
            }

            if (!isPoint) {
                if (i == 0) {
                    path.moveTo(cx, cy);
                } else {
                    path.lineTo(cx, cy);
                }
            }


        }


        if (!isPoint) {
            painPoint.setStyle(Paint.Style.STROKE);
            canvas.drawPath(path, painPoint);
            path.reset();
        }


    }

    private void drawColumn(Canvas canvas, Paint paint) {
        double v = 0;

        float cellWidth = width / axisDivideSizeX;
        float cellHeight = mShapeHeight / axisDivideSizeY;

        for (int i = 0; i < nuringInfoShapeBeen.size(); i++) {

            paint.setColor(getContext().getResources().getColor(R.color.titlecolor));
            float leftX = (cellWidth * (i + 1) + originX) - (cellWidth / 4);
            float rightX = (cellWidth * (i + 1) + originX) + (cellWidth / 4);

            int i1 = yLabels[yLabels.length - 1] - yLabels[0];
            float v2 = i1 / (cellHeight * (yLabels.length - 1));
            if (!TextUtils.isEmpty(nuringInfoShapeBeen.get(i).getValue())) {
                v = Float.parseFloat(nuringInfoShapeBeen.get(i).getValue()) - yLabels[0];
                double v1 = v / v2;
                float leftTopY = (originY + mShapeHeight) - ((float) v1);
                float bottmoY = originY + mShapeHeight;
                canvas.drawRect(leftX, leftTopY, rightX, bottmoY, mPaint);
            } else {
                break;
            }

        }
    }
}
