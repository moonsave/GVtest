package custom.android.view;

/*******************************************************************************
 * Copyright (c) 2016 Thuat Nguyen
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 *******************************************************************************/

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.example.gvtest.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class GaugeView extends View {

    public static final int SIZE = 300;
    public static final float TOP = 0.0f;
    public static final float LEFT = 0.0f;
    public static final float RIGHT = 1.0f;
    public static final float BOTTOM = 1.0f;
    public static final float CENTER = 0.5f;
    public static final boolean SHOW_OUTER_SHADOW = true;
    public static final boolean SHOW_OUTER_BORDER = true;
    public static final boolean SHOW_OUTER_RIM = true;
    public static final boolean SHOW_INNER_RIM = true;
    public static final boolean SHOW_NEEDLE = true;
    public static final boolean SHOW_SCALE = false;
    public static final boolean SHOW_RANGES = true;
    public static final boolean SHOW_GAUGE_RANGES = true;
    public static final boolean SHOW_TEXT = false;
    public static final boolean DEFAULT_SHOW_RANGE_VALUES = true;
    public static final float OUTER_SHADOW_WIDTH = 0.03f;
    public static final float OUTER_BORDER_WIDTH = 0.04f;
    public static final float OUTER_RIM_WIDTH = 0.05f;
    public static final float INNER_RIM_WIDTH = 0.06f;
    public static final float INNER_RIM_BORDER_WIDTH = 0.005f;
    public static final float NEEDLE_WIDTH = 0.02f;
    public static final float NEEDLE_HEIGHT = 0.3f;
    public static final int INNER_CIRCLE_COLOR = Color.rgb(190, 215, 123);
    public static final int OUTER_CIRCLE_COLOR = Color.rgb(205, 231, 132);
    public static final float SCALE_POSITION = 0.015f; //틱 반지름 작을수록 넓어짐
    public static final float SCALE_START_VALUE = 0.0f;
    public static final float SCALE_END_VALUE = 100.0f;
    public static final float SCALE_START_ANGLE = 60.0f;
    public static final int SCALE_DIVISIONS = 10;
    public static final int SCALE_SUBDIVISIONS = 5;

    public static final int[] RANGE_COLORS = {Color.WHITE};

    public static final int TEXT_SHADOW_COLOR = Color.argb(100, 0, 0, 0);
    public static final int TEXT_VALUE_COLOR = Color.WHITE;
    public static final int TEXT_UNIT_COLOR = Color.WHITE;
    public static final float TEXT_VALUE_SIZE = 0.3f;
    public static final float TEXT_UNIT_SIZE = 0.1f;
    private static final float[] DEFAULT_RANGE_VALUES = new float[]{0.0f, 100.0f, 200.0f, 500.0f, 1000.0f, 3000.0f, 5000.0f};
    private static final int DEFAULT_COLOR = Color.parseColor("#3498db");
    private static final int DEFAULT_FACE_COLOR = Color.parseColor("#000000"); //반원 계기판 내부


    // *--------------------------------------------------------------------- *//
    // Customizable properties
    // *--------------------------------------------------------------------- *//

    private boolean mShowOuterShadow;
    private boolean mShowOuterBorder;
    private boolean mShowOuterRim;
    private boolean mShowInnerRim;
    private boolean mShowScale;
    private boolean mShowRanges;
    private boolean mShowGaugeRanges;
    private boolean mShowNeedle;
    private boolean mShowText;

    private float mOuterShadowWidth;
    private float mOuterBorderWidth;
    private float mOuterRimWidth;
    private float mInnerRimWidth;
    private float mInnerRimBorderWidth;
    private float mNeedleWidth;
    private float mNeedleHeight;
    private int mInnerCircleColor;
    private int mOuterCircleColor;

    private float mScalePosition;
    private float mScaleStartValue;
    private float mScaleEndValue;
    private float mScaleStartAngle;
    private float[] mRangeValues;

    private int[] mRangeColors;
    private int mDivisions;
    private int mSubdivisions;

    private RectF mOuterShadowRect;
    private RectF mOuterBorderRect;
    private RectF mOuterRimRect;
    private RectF mInnerRimRect;
    private RectF mInnerRimBorderRect;
    private RectF mFaceRect;
    private RectF mScaleRect;

    private Bitmap mBackground;
    private Paint mBackgroundPaint;
    private Paint mOuterShadowPaint;
    private Paint mOuterBorderPaint;
    private Paint mOuterRimPaint;
    private Paint mInnerRimPaint;
    private Paint mInnerRimBorderLightPaint;
    private Paint mInnerRimBorderDarkPaint;
    private Paint mFacePaint;
    private Paint mFaceBorderPaint;
    private Paint mFaceShadowPaint;
    private Paint[] mRangePaints;
    private Paint mNeedleRightPaint;
    private Paint mNeedleLeftPaint;
    private Paint mNeedleScrewPaint;
    private Paint mNeedleScrewBorderPaint;
    private Paint mTextValuePaint;
    private Paint mTextUnitPaint;

    private String mTextValue;
    private String mTextUnit;
    private int mTextValueColor;
    private int mTextUnitColor;
    private int mTextShadowColor;
    private float mTextValueSize;
    private float mTextUnitSize;

    private Path mNeedleRightPath;
    private Path mNeedleLeftPath;

    // *--------------------------------------------------------------------- *//

    private float mScaleRotation;
    private float mDivisionValue;
    private float mSubdivisionValue;
    private float mSubdivisionAngle;

    private float mTargetValue;
    private float mCurrentValue;

    private float mNeedleVelocity;
    private float mNeedleAcceleration;
    private long mNeedleLastMoved = -1;
    private boolean mNeedleInitialized;
    private RectF mDynamicBorderRect;
    private boolean mShowRangeValues;

    // CUSTOM *--------------------------------------------------------------------- *//

    private int minValue = 0;
    private int maxValue = 100;

    private final float startAngle = 148; //시작앵글
    private final float sweepAngle = 244; //종료 앵글

    private final float ARC_STROKE_WIDTH = 0.05f; //게이지 굵기 조절 1에 가까울수록 굵어짐 *수치변경시 initDrawingRects() 함수안 add 수치도 조절해야함

    private List<GaugeRange> gaugeRangeList = new ArrayList<>();

    public GaugeView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        readAttrs(context, attrs, defStyle);
        init();
    }

    public GaugeView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GaugeView(final Context context) {
        this(context, null, 0);
    }

    private void readAttrs(final Context context, final AttributeSet attrs, final int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GaugeView, defStyle, 0);
        mShowOuterShadow = a.getBoolean(R.styleable.GaugeView_showOuterShadow, SHOW_OUTER_SHADOW);
        mShowOuterBorder = a.getBoolean(R.styleable.GaugeView_showOuterBorder, SHOW_OUTER_BORDER);
        mShowOuterRim = a.getBoolean(R.styleable.GaugeView_showOuterRim, SHOW_OUTER_RIM);
        mShowInnerRim = a.getBoolean(R.styleable.GaugeView_showInnerRim, SHOW_INNER_RIM);
        mShowNeedle = a.getBoolean(R.styleable.GaugeView_showNeedle, SHOW_NEEDLE);
        mShowScale = a.getBoolean(R.styleable.GaugeView_showScale, SHOW_SCALE);
        mShowRanges = a.getBoolean(R.styleable.GaugeView_showRanges, SHOW_RANGES);
        mShowGaugeRanges = a.getBoolean(R.styleable.GaugeView_showGaugeRanges, SHOW_GAUGE_RANGES);
        mShowText = a.getBoolean(R.styleable.GaugeView_showRangeText, DEFAULT_SHOW_RANGE_VALUES);

        mOuterShadowWidth = mShowOuterShadow ? a.getFloat(R.styleable.GaugeView_outerShadowWidth, OUTER_SHADOW_WIDTH) : 0.0f;
        mOuterBorderWidth = mShowOuterBorder ? a.getFloat(R.styleable.GaugeView_outerBorderWidth, OUTER_BORDER_WIDTH) : 0.0f;
        mOuterRimWidth = mShowOuterRim ? a.getFloat(R.styleable.GaugeView_outerRimWidth, OUTER_RIM_WIDTH) : 0.0f;
        mInnerRimWidth = mShowInnerRim ? a.getFloat(R.styleable.GaugeView_innerRimWidth, INNER_RIM_WIDTH) : 0.0f;
        mInnerRimBorderWidth = mShowInnerRim ? a.getFloat(R.styleable.GaugeView_innerRimBorderWidth, INNER_RIM_BORDER_WIDTH) : 0.0f;

        mNeedleWidth = a.getFloat(R.styleable.GaugeView_needleWidth, NEEDLE_WIDTH);
        mNeedleHeight = a.getFloat(R.styleable.GaugeView_needleHeight, NEEDLE_HEIGHT);
        mInnerCircleColor = a.getColor(R.styleable.GaugeView_innerCircleColor, INNER_CIRCLE_COLOR);
        mOuterCircleColor = a.getColor(R.styleable.GaugeView_outerCircleColor, OUTER_CIRCLE_COLOR);


        mScalePosition = (mShowScale || mShowRanges) ? a.getFloat(R.styleable.GaugeView_scalePosition, SCALE_POSITION) : 0.0f;
        mScaleStartValue = a.getFloat(R.styleable.GaugeView_scaleStartValue, SCALE_START_VALUE);
        mScaleEndValue = a.getFloat(R.styleable.GaugeView_scaleEndValue, SCALE_END_VALUE);
        mScaleStartAngle = a.getFloat(R.styleable.GaugeView_scaleStartAngle, SCALE_START_ANGLE);

        mDivisions = a.getInteger(R.styleable.GaugeView_divisions, SCALE_DIVISIONS);
        mSubdivisions = a.getInteger(R.styleable.GaugeView_subdivisions, SCALE_SUBDIVISIONS);

        if (mShowRanges) {
            mTextShadowColor = a.getColor(R.styleable.GaugeView_textShadowColor, TEXT_SHADOW_COLOR);

            final CharSequence[] rangeValues = a.getTextArray(R.styleable.GaugeView_rangeValues);

            final CharSequence[] rangeColors = a.getTextArray(R.styleable.GaugeView_rangeColors);
            readRanges(rangeValues, rangeColors);
            mShowRangeValues = DEFAULT_SHOW_RANGE_VALUES;
        }

        if (mShowText) {
            final int textValueId = a.getResourceId(R.styleable.GaugeView_textValue, 0);
            final String textValue = a.getString(R.styleable.GaugeView_textValue);
            mTextValue = (0 < textValueId) ? context.getString(textValueId) : (null != textValue) ? textValue : "";

            final int textUnitId = a.getResourceId(R.styleable.GaugeView_textUnit, 0);
            final String textUnit = a.getString(R.styleable.GaugeView_textUnit);
            mTextUnit = (0 < textUnitId) ? context.getString(textUnitId) : (null != textUnit) ? textUnit : "";
            mTextValueColor = a.getColor(R.styleable.GaugeView_textValueColor, TEXT_VALUE_COLOR);
            mTextUnitColor = a.getColor(R.styleable.GaugeView_textUnitColor, TEXT_UNIT_COLOR);
            mTextShadowColor = a.getColor(R.styleable.GaugeView_textShadowColor, TEXT_SHADOW_COLOR);

            mTextValueSize = a.getFloat(R.styleable.GaugeView_textValueSize, TEXT_VALUE_SIZE);
            mTextUnitSize = a.getFloat(R.styleable.GaugeView_textUnitSize, TEXT_UNIT_SIZE);
        }

        a.recycle();
    }

    private void readRanges(final CharSequence[] rangeValues, final CharSequence[] rangeColors) {

        int rangeValuesLength;
        if (rangeValues == null) {
            rangeValuesLength = DEFAULT_RANGE_VALUES.length;
        } else {
            rangeValuesLength = rangeValues.length;
        }
        final int length = rangeValuesLength;
        if (rangeValues != null) {
            mRangeValues = new float[length];
            for (int i = 0; i < length; i++) {
                mRangeValues[i] = Float.parseFloat(rangeValues[i].toString());
            }
        } else {
            mRangeValues = DEFAULT_RANGE_VALUES;
        }

        if (rangeColors != null) {
            mRangeColors = new int[length];
            for (int i = 0; i < length; i++) {
                mRangeColors[i] = Color.parseColor(rangeColors[i].toString());
            }
        } else {
            mRangeColors = RANGE_COLORS;
        }
    }

    @TargetApi(11)
    private void init() {
        // TODO Why isn't this working with HA layer?
        // The needle is not displayed although the onDraw() is being triggered by invalidate()
        // calls.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initDrawingRects();
        initDrawingTools();

        // Compute the scale properties
        if (mShowRanges) {
            initScale();
        }
    }

    public void initDrawingRects() {
        // The drawing area is a rectangle of width 1 and height 1,
        // where (0,0) is the top left corner of the canvas.
        // Note that on Canvas X axis points to right, while the Y axis points downwards.
        mOuterShadowRect = new RectF(LEFT, TOP, RIGHT, BOTTOM );

        final float add = 0.025f; // 0에 가까울 수록 게이지 반지름이 커짐
        final float add_p = 0.00f;

        //하얀색게이지
        mOuterBorderRect = new RectF(mOuterShadowRect.left + mOuterShadowWidth + add , mOuterShadowRect.top + mOuterShadowWidth + add,
                mOuterShadowRect.right - mOuterShadowWidth - add, mOuterShadowRect.bottom - mOuterShadowWidth - add );
        //
        mDynamicBorderRect = new RectF(mOuterBorderRect.left + add_p, mOuterBorderRect.top + add_p, mOuterBorderRect.right - add_p, mOuterBorderRect.bottom - add_p);
        //계기판 외부
        mOuterRimRect = new RectF(mOuterBorderRect.left + mOuterBorderWidth, mOuterBorderRect.top + mOuterBorderWidth,
                mOuterBorderRect.right - mOuterBorderWidth, mOuterBorderRect.bottom - mOuterBorderWidth);
        //계기판 내부
        mInnerRimRect = new RectF(mOuterRimRect.left + mOuterRimWidth, mOuterRimRect.top + mOuterRimWidth, mOuterRimRect.right
                - mOuterRimWidth, mOuterRimRect.bottom - mOuterRimWidth);
        //
        mInnerRimBorderRect = new RectF(mInnerRimRect.left + mInnerRimBorderWidth, mInnerRimRect.top + mInnerRimBorderWidth,
                mInnerRimRect.right - mInnerRimBorderWidth, mInnerRimRect.bottom - mInnerRimBorderWidth);
        //게이지 배경
        mFaceRect = new RectF(mInnerRimRect.left + mInnerRimWidth - add, mInnerRimRect.top + mInnerRimWidth - add,
                mInnerRimRect.right - mInnerRimWidth + add, mInnerRimRect.bottom - mInnerRimWidth + add);
        //틱
        mScaleRect = new RectF(mFaceRect.left + mScalePosition, mFaceRect.top + mScalePosition, mFaceRect.right - mScalePosition,
                mFaceRect.bottom - mScalePosition);
    }

    private void initDrawingTools() {
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setFilterBitmap(true);

        if (mShowOuterShadow) {
            mOuterShadowPaint = getDefaultOuterShadowPaint();
        }
        mOuterBorderPaint = getDefaultOuterBorderPaint();

        if (mShowOuterRim) {
            mOuterRimPaint = getDefaultOuterRimPaint();
        }
        if (mShowInnerRim) {
            mInnerRimPaint = getDefaultInnerRimPaint();
            mInnerRimBorderLightPaint = getDefaultInnerRimBorderLightPaint();
            mInnerRimBorderDarkPaint = getDefaultInnerRimBorderDarkPaint();
        }
        if (mShowRanges) {
            setDefaultScaleRangePaints();
        }
        if (mShowNeedle) {
            setDefaultNeedlePaths();
            mNeedleLeftPaint = getDefaultNeedleLeftPaint();
            mNeedleRightPaint = getDefaultNeedleRightPaint();
            mNeedleScrewPaint = getDefaultNeedleScrewPaint();
            mNeedleScrewBorderPaint = getDefaultNeedleScrewBorderPaint();
        }
        if (mShowText) {
            mTextValuePaint = getDefaultTextValuePaint();
            mTextUnitPaint = getDefaultTextUnitPaint();
        }

        mFacePaint = getDefaultFacePaint();
        mFaceBorderPaint = getDefaultFaceBorderPaint();
        mFaceShadowPaint = getDefaultFaceShadowPaint();
    }

    public Paint getDefaultOuterShadowPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(1f);
        return paint;
    }

    private Paint getDefaultOuterBorderPaint() {
        //게이지 배경 페인트
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.parseColor("#1AFFFFFF"));
        //paint.setColor(Color.WHITE);
        paint.setStrokeWidth(ARC_STROKE_WIDTH);
        paint.setStrokeCap(Paint.Cap.ROUND);

        return paint;
    }

    public Paint getDefaultOuterRimPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setFilterBitmap(true);
        return paint;
    }

    private Paint getDefaultInnerRimPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(1f);
        return paint;
    }

    private Paint getDefaultInnerRimBorderLightPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(1f);

        return paint;
    }

    private Paint getDefaultInnerRimBorderDarkPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(1f);
        return paint;
    }

    public Paint getDefaultFacePaint() {
        //게이지 내부 배경 페인트 설정
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(DEFAULT_FACE_COLOR);
        paint.setColor(Color.parseColor("#00000000")); //tnwjd #00000000
        return paint;
    }

    public Paint getDefaultFaceBorderPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(0.02f);
        return paint;
    }

    public Paint getDefaultFaceShadowPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(DEFAULT_COLOR);
        paint.setStrokeWidth(0.03f);

        return paint;
    }

    public void setDefaultNeedlePaths() {
        final float x = 0.5f, y = 0.5f;
        //바늘 그리기 위치정보
        mNeedleLeftPath = new Path();
        mNeedleLeftPath.moveTo(x, y);
        mNeedleLeftPath.lineTo(x - mNeedleWidth, y);
        mNeedleLeftPath.lineTo(x, y - mNeedleHeight);
        mNeedleLeftPath.lineTo(x, y);
        mNeedleLeftPath.lineTo(x - mNeedleWidth, y);

        mNeedleRightPath = new Path();
        mNeedleRightPath.moveTo(x, y);
        mNeedleRightPath.lineTo(x + mNeedleWidth, y);
        mNeedleRightPath.lineTo(x, y - mNeedleHeight);
        mNeedleRightPath.lineTo(x, y);
        mNeedleRightPath.lineTo(x + mNeedleWidth, y);
    }

    public Paint getDefaultNeedleLeftPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#16B2B6")); //바늘 왼쪽 색
        return paint;
    }

    public Paint getDefaultNeedleRightPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#16B2B6")); //바늘 오른쪽 색
        return paint;
    }

    public Paint getDefaultNeedleScrewPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#16B2B6")); //바늘 구체 색
        return paint;
    }

    public Paint getDefaultNeedleScrewBorderPaint() {
        final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.TRANSPARENT);
        paint.setStrokeWidth(0.005f);
        return paint;
    }


    public void setDefaultScaleRangePaints() {
        //division range text paint 틱 범위 표기 텍스트 페인트
        final int length = mRangeColors.length;
        mRangePaints = new Paint[length];
        for (int i = 0; i < length; i++) {
            mRangePaints[i] = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
            mRangePaints[i].setColor(mRangeColors[i]);
            mRangePaints[i].setStyle(Paint.Style.STROKE);
            mRangePaints[i].setStrokeWidth(0.005f);
            mRangePaints[i].setTextSize(0.05f);

            //mRangePaints[i].setTypeface(Typeface.SANS_SERIF); //기존 폰트
            // mRangePaints[i].setTypeface( Typeface.createFromAsset( this.getContext().getAssets(), "roboto_regular.ttf" ) ); //폰트 변경
            mRangePaints[i].setTypeface( ResourcesCompat.getFont(this.getContext(), R.font.roboto_regular) ); //폰트 변경

            mRangePaints[i].setTextAlign(Align.CENTER);
            mRangePaints[i].setShadowLayer(0.005f, 0.002f, 0.002f, mTextShadowColor);
        }
    }


    public Paint getDefaultTextValuePaint() {
        //value text paint 메인 밸류 텍스트 페인트
        final Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTextValueColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(mTextValueSize);
        paint.setTextAlign(Align.CENTER);
        //paint.setTypeface(Typeface.SANS_SERIF);
        // paint.setTypeface( Typeface.createFromAsset( this.getContext().getAssets(), "roboto_medium.ttf" ) ); //폰트 변경
        paint.setTypeface( ResourcesCompat.getFont(this.getContext(), R.font.roboto_medium)); //폰트 변경
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, mTextShadowColor);
        return paint;
    }

    public Paint getDefaultTextUnitPaint() {
        //unit text paint 단위 텍스트 페인트
        final Paint paint = new Paint(Paint.LINEAR_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setColor(mTextUnitColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(0.005f);
        paint.setTextSize(mTextUnitSize);
        paint.setTextAlign(Align.CENTER);
        paint.setTypeface(Typeface.SANS_SERIF);
        //paint.setTypeface( Typeface.createFromAsset( this.getContext().getAssets(), "roboto_medium.ttf" ) ); //폰트 변경
        paint.setShadowLayer(0.01f, 0.002f, 0.002f, mTextShadowColor);
        return paint;
    }

    @Override
    protected void onRestoreInstanceState(final Parcelable state) {
        final Bundle bundle = (Bundle) state;
        final Parcelable superState = bundle.getParcelable("superState");
        super.onRestoreInstanceState(superState);

        mNeedleInitialized = bundle.getBoolean("needleInitialized");
        mNeedleVelocity = bundle.getFloat("needleVelocity");
        mNeedleAcceleration = bundle.getFloat("needleAcceleration");
        mNeedleLastMoved = bundle.getLong("needleLastMoved");
        mCurrentValue = bundle.getFloat("currentValue");
        mTargetValue = bundle.getFloat("targetValue");
    }

    private void initScale() {
        mScaleRotation = (mScaleStartAngle + 180) % 360;
        mDivisionValue = (mScaleEndValue - mScaleStartValue) / mDivisions;
        Log.d("mDivisionValue:", String.valueOf(mDivisionValue));
        mSubdivisionValue = mDivisionValue / mSubdivisions;
        mSubdivisionAngle = (360 - 2 * mScaleStartAngle) / (mDivisions * mSubdivisions);
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        final Parcelable superState = super.onSaveInstanceState();

        final Bundle state = new Bundle();
        state.putParcelable("superState", superState);
        state.putBoolean("needleInitialized", mNeedleInitialized);
        state.putFloat("needleVelocity", mNeedleVelocity);
        state.putFloat("needleAcceleration", mNeedleAcceleration);
        state.putLong("needleLastMoved", mNeedleLastMoved);
        state.putFloat("currentValue", mCurrentValue);
        state.putFloat("targetValue", mTargetValue);
        return state;
    }

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {
        final int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        final int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        final int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        final int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        final int chosenWidth = chooseDimension(widthMode, widthSize);
        final int chosenHeight = chooseDimension(heightMode, heightSize);
        setMeasuredDimension(chosenWidth, chosenHeight);
    }

    private int chooseDimension(final int mode, final int size) {
        switch (mode) {
            case View.MeasureSpec.AT_MOST:
            case View.MeasureSpec.EXACTLY:
                return size;
            case View.MeasureSpec.UNSPECIFIED:
            default:
                return getDefaultDimension();
        }
    }

    private int getDefaultDimension() {
        return SIZE;
    }

    @Override
    protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
        drawGauge();
    }

    private void drawGauge() {
        //게이지 배경, 원형틀, 틱 그리기
        if (null != mBackground) {
            // Let go of the old background
            mBackground.recycle();
        }
        // Create a new background according to the new width and height
        mBackground = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(mBackground);
        setCanvasScale(canvas);

//        final float scale = Math.min(getWidth(), getHeight());
//
//        //canvas.scale(getWidth(), getHeight()); //크기만큼 확대
//
//        canvas.scale(scale, scale); //작은 축을 기준으로 확대
//        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
//                //x축보다 y축이 더 짧다면 그 만큼 x축 이동
//                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);
//        //반대로 계산식 만큼 y축 이동
//
//        canvas.translate(0f, 0.15f); //계기판 백그라운드, 텍스트위치 이동

        drawRim(canvas);
        drawFace(canvas);

        if (mShowRanges) {
            drawScale(canvas);
        }

    }
    private void setCanvasScale(Canvas canvas) {
        final float scale = Math.min(getWidth(), getHeight());

        if(getWidth() > getHeight()) {
            canvas.scale(getWidth(), getHeight() + ((scale / 5)));
        } else if (getWidth() < getHeight()) {
            canvas.scale(getWidth(), getHeight() + ((scale / 5)));
        } else if (getWidth() == getHeight()) {
            canvas.scale(scale, scale + ((scale / 5)));
        }

        //canvas.scale(getWidth(), getHeight()); //크기만큼 확대
        /*canvas.scale(scale, scale + 100); //작은 축을 기준으로 확대
        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
                //x축보다 y축이 더 짧다면 그 만큼 x축 이동
                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);
                //반대로 계산식 만큼 y축 이동*/

        //canvas.translate(0f, 0.15f); //계기판 백그라운드, 텍스트위치 이동
    }
    @Override
    protected void onDraw(final Canvas canvas) {
        //바늘 , 게이지(색) 그리기
        drawBackground(canvas);

        setCanvasScale(canvas);
//        final float scale = Math.min(getWidth(), getHeight());
//        //canvas.scale(getWidth(), getHeight());
//
//        canvas.scale(scale, scale);
//        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
//                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);
//
//        canvas.translate(0f, 0.15f); //색 게이지와 바늘 위치 이동
        if (mShowRanges) {
            if(mShowGaugeRanges) {
                drawGaugeRanges(canvas);
            }
            if (mShowNeedle) {
                drawNeedle(canvas);
            }

            if (mShowText) {
                drawText(canvas);
            }

            computeCurrentValue();
        }
    }

    private void drawBackground(final Canvas canvas) {
        if (null != mBackground) {
            canvas.drawBitmap(mBackground, 0, 0, mBackgroundPaint);
        }
    }

    private void drawRim(final Canvas canvas) {
        canvas.drawArc(mOuterBorderRect, startAngle, sweepAngle, false, mOuterBorderPaint);
    }

    private void drawFace(final Canvas canvas) {
        // Draw the face gradient
        canvas.drawArc(mFaceRect, startAngle, sweepAngle, false, mFacePaint);
    }

    private void drawText(final Canvas canvas) {
        final String textValue = !TextUtils.isEmpty(mTextValue) ? mTextValue : valueString(mCurrentValue);
        final float textValueWidth = mTextValuePaint.measureText(textValue);
        final float textUnitWidth = !TextUtils.isEmpty(mTextUnit) ? mTextUnitPaint.measureText(mTextUnit) : 0;

        final float startX = CENTER;
        final float startY = CENTER + 0.2f;
        drawText(canvas, -1, textValue, startX, startY, mTextValuePaint);

        if (!TextUtils.isEmpty(mTextUnit)) {
            drawText(canvas, -1, mTextUnit, CENTER, CENTER + 0.3f, mTextUnitPaint);
        }
    }

    private void drawScale(final Canvas canvas) {
        canvas.save();
        //canvas.save(Canvas.MATRIX_SAVE_FLAG);
        // On canvas, North is 0 degrees, East is 90 degrees, South is 180 etc.
        // We start the scale somewhere South-West so we need to first rotate the canvas.
        canvas.rotate(mScaleRotation, 0.5f, 0.5f);
        Log.d("mScaleRotation: ", String.valueOf(mScaleRotation));

        final int totalTicks = mDivisions * mSubdivisions + 1 ;
        for (int i = 0; i < totalTicks; i++) {
            final float y1 = mScaleRect.top;
            Log.d("mScaleRect.top: ", String.valueOf(mScaleRect.top));

            final float y2 = y1 + 0.045f; // height of division
            final float y3 = y1 + 0.090f; // height of subdivision

            final float value = getValueForTick(i);
            final Paint paint = getRangePaint(value);

            float div = mScaleEndValue / (float) mDivisions;
            float mod = value % div;
            // Draw a division tick
            paint.setStrokeWidth(0.01f); //틱 굵기
            paint.setColor(Color.parseColor("#99FFFFFF")); //division 틱 색상
            paint.setStrokeCap(Paint.Cap.ROUND); //모서리 둥글게

            //canvas.drawLine(0.5f, y1 - 0.04f, 0.5f, y3 - 0.06f, paint); //틱이 게이지 안에
            canvas.drawLine(0.5f, y1 - 0.01f, 0.5f, y3 - 0.06f, paint);

            // Draw the text 0.15 away from the division tick
            paint.setStyle(Paint.Style.FILL);
            if (mShowRangeValues) {
                drawText(canvas, i, valueString(value), 0.5f, y3, paint); //틱 밸류 표기 페인트
            }
            canvas.rotate(mSubdivisionAngle, 0.5f, 0.5f);
        }
        canvas.restore();
    }

    private void drawText(Canvas canvas, int tick, String value, float x, float y, Paint paint) {
        //Save original font size
        float originalTextSize = paint.getTextSize();

        // set a magnification factor
        final float magnifier = 100f;

        // Scale the canvas
        canvas.save();
        canvas.scale(1f / magnifier, 1f / magnifier);
        float textWidth = 0;
        float textHeight = 0;
        if (tick != -1) {
//            textWidth = paint.measureText(value);
            final int middleValue = mRangeValues.length / 2;
            if (tick == middleValue) {
                textHeight = -1;
                textWidth = 0;
            } else {
                textHeight = 1.5f;
                textWidth = -1;
            }
            canvas.rotate(120 - tick * mSubdivisionAngle, x * magnifier, y * magnifier);
        }
        // increase the font size
        paint.setTextSize(originalTextSize * magnifier);

        // [20231107] 나무요청으로 포멧 적용
        String formatValue = value.replaceAll("\\B(?=(\\d{3})+(?!\\d))", ",");
        canvas.drawText(formatValue, x * magnifier + textWidth, y * magnifier + textHeight, paint);

        // bring everything back to normal
        canvas.restore();
        paint.setTextSize(originalTextSize);
    }

    /**
     * Draws a text in the canvas with spacing between each letter.
     * Basically what this method does is it split's the given text into individual letters
     * and draws each letter independently using Canvas.drawText with a separation of
     * {@code spacingX} between each letter.
     *
     * @param canvas    the canvas where the text will be drawn
     * @param text      the text what will be drawn
     * @param left      the left position of the text
     * @param top       the top position of the text
     * @param paint     holds styling information for the text
     * @param spacingPx the number of pixels between each letter that will be drawn
     */
    public static void drawSpacedText(Canvas canvas, String text, float left, float top, Paint paint, float spacingPx) {

        float currentLeft = left;

        for (int i = 0; i < text.length(); i++) {
            String c = text.charAt(i) + "";
            canvas.drawText(c, currentLeft, top, paint);
            currentLeft += spacingPx;
            currentLeft += paint.measureText(c);
        }
    }

    /**
     * returns the width of a text drawn by drawSpacedText
     */
    public static float getSpacedTextWidth(Paint paint, String text, float spacingX) {
        return paint.measureText(text) + spacingX * (text.length() - 1);
    }

    private String valueString(final float value) {
        return String.format("%d", (int) value);
    }

    private float getValueForTick(final int tick) {
        return mRangeValues[tick];
    }
    protected Paint getRangePaint(int color) {
        Paint range = new Paint(Paint.ANTI_ALIAS_FLAG);
        range.setColor(color);
        range.setStyle(Paint.Style.STROKE);
        range.setStrokeWidth(ARC_STROKE_WIDTH);
        range.setStrokeCap(Paint.Cap.ROUND);
        return range;
    }
    private Paint getRangePaint(final float value) {
        return mRangePaints[0];
    }

    private void drawNeedle(final Canvas canvas) {
        if (mNeedleInitialized) {
            final float angle = getAngleForValue(mCurrentValue);
            final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(Color.parseColor("#FF00FF")); //progress 채워지는색
            paint.setStrokeWidth(ARC_STROKE_WIDTH);
            paint.setStrokeCap(Paint.Cap.ROUND); //progress 둥글게
            float sweepAngle;

            if (angle != 0 && angle != 240) {
                if (angle < 240) {
                    sweepAngle = angle + 120;
                } else {
                    sweepAngle = angle - 240;
                }
                //canvas.drawArc(mDynamicBorderRect, startAngle, sweepAngle, false, paint); //게이지바 채워지는 progress 그리기
            }
            canvas.save();

            canvas.rotate(angle, 0.5f, 0.5f);

            setNeedleShadowPosition(angle);
            canvas.drawPath(mNeedleLeftPath, mNeedleLeftPaint);
            canvas.drawPath(mNeedleRightPath, mNeedleRightPaint);

            canvas.restore();

            // Draw the needle screw and its border
            canvas.drawCircle(0.5f, 0.5f, 0.03f, mNeedleScrewPaint); //바늘 구체 그리기
        }
    }

    private void setNeedleShadowPosition(final float angle) {
        if (angle > 180 && angle < 360) {
            // Move shadow from right to left
            mNeedleRightPaint.setShadowLayer(0, 0, 0, Color.BLACK);
            mNeedleLeftPaint.setShadowLayer(0.01f, -0.005f, 0.005f, Color.argb(127, 0, 0, 0));
        } else {
            // Move shadow from left to right
            mNeedleLeftPaint.setShadowLayer(0, 0, 0, Color.BLACK);
            mNeedleRightPaint.setShadowLayer(0.01f, 0.005f, -0.005f, Color.argb(127, 0, 0, 0));
        }
    }

    private float getAngleForValue(final float value) {
        if (!mShowRanges)
            return -1;
        int range = -1;
        for (int i = 0; i < mRangeValues.length - 1; ++i) {
            range++;
            if (value == mRangeValues[i]) {
                range = i;
                break;
            }
            if (value == mRangeValues[i + 1]) {
                range = i + 1;
                break;
            }
            if (value > mRangeValues[i] && value < mRangeValues[i + 1]) {
                break;
            }
        }

        if (range == -1)
            return 0;
        // TODO: mRangeValues[range + 1] , mRangeValues.length() 랑 range가 같으면 range는 -1해주어야한다.
        if (range >= mRangeValues.length-1) {
            --range;
        }
        float angle = range * mSubdivisionAngle + (value - mRangeValues[range]) * mSubdivisionAngle / (mRangeValues[range + 1] - mRangeValues[range]);

        return (mScaleRotation + angle) % 360;
    }

    private void computeCurrentValue() {
        // Logger.log.warn(String.format("velocity=%f, acceleration=%f", mNeedleVelocity,
        // mNeedleAcceleration));

        if (!(Math.abs(mCurrentValue - mTargetValue) > 0.01f)) {
            return;
        }
        //바늘이동 애니메이션 & 텍스트 애니메이션
        if (-1 != mNeedleLastMoved) {
            final float time = (System.currentTimeMillis() - mNeedleLastMoved) / 1000.0f;
            final float direction = Math.signum(mNeedleVelocity);
            if (Math.abs(mNeedleVelocity) < 90.0f) {
                mNeedleAcceleration = 5.0f * (mTargetValue - mCurrentValue);
            } else {
                mNeedleAcceleration = 0.0f;
            }

            mNeedleAcceleration = 5.0f * (mTargetValue - mCurrentValue);
            mCurrentValue += mNeedleVelocity * time;
            mNeedleVelocity += mNeedleAcceleration * time;

            if ((mTargetValue - mCurrentValue) * direction < 0.01f * direction) {
                mCurrentValue = mTargetValue;
                mNeedleVelocity = 0.0f;
                mNeedleAcceleration = 0.0f;
                mNeedleLastMoved = -1L;
            } else {
                mNeedleLastMoved = System.currentTimeMillis();
            }

            invalidate();

        } else {
            mNeedleLastMoved = System.currentTimeMillis();
            computeCurrentValue();
        }
    }

    public void setTargetValue(final float value) {
        if (mShowScale || mShowRanges) {
            if (value < mScaleStartValue) {
                mTargetValue = mScaleStartValue;
            } else if (value > mScaleEndValue) {
                mTargetValue = mScaleEndValue;
            } else {
                mTargetValue = value;
            }
        } else {
            mTargetValue = value;
        }
        mNeedleInitialized = true;
        invalidate();
    }

    public void setShowRangeValues(boolean mShowRangeValues) {
        this.mShowRangeValues = mShowRangeValues;
        mNeedleInitialized = true;
        invalidate();
    }

    //함수 추가 *---------------------------------------------------- *//
    public void setMinValue(int value) {
        this.mScaleStartValue = value;
        this.minValue = value;
    }
    public int getMinValue() { return this.minValue; }
    public void setMaxValue(int value) {
        this.mScaleEndValue = value;
        this.maxValue = value;
    }
    public int getMaxValue() { return this.maxValue; }
    public void addGaugeRange(GaugeRange range) {
        if (range == null)
            return;
        gaugeRangeList.add(range);
    }
    public List<GaugeRange> getGaugeRanges() {
        return gaugeRangeList;
    }
    public void setGaugeRanges(List<GaugeRange> ranges) {
        this.gaugeRangeList = ranges;
    }

    public void setRangeValues(float[] rangeValues) {
        this.mRangeValues = rangeValues;
    }
    public float[] getRangeValues() {
        return this.mRangeValues;
    }
    public void setDivisions(int value) { this.mDivisions = value; }
    public int getDivisions() { return this.mDivisions; }

    private void drawGaugeRanges(Canvas canvas) {
        if (gaugeRangeList.size() < 1) {
            return;
        }
        for (GaugeRange range : getGaugeRanges()) {
            float startAngle = calculateStartAngle(range.getMinValue());
            float sweepAngle = calculateSweepAngle(range.getMinValue(), range.getMaxValue());
            canvas.drawArc(mDynamicBorderRect, startAngle, sweepAngle, false, getRangePaint(range.getColor()));
        }
    }
    private float calculateStartAngle(double from) {
        return sweepAngle / 100 * getCalculateValuePercentage(from) + startAngle;
        // 244 / 100 * 10 + 148
    }

    private float calculateSweepAngle(double from, double to) {
        return sweepAngle / 100 * getCalculateValuePercentage(to) - sweepAngle / 100 * getCalculateValuePercentage(from) + 0.5f;

    }

    protected int getCalculateValuePercentage(double value) {
        return getCalculateValuePercentage(getMinValue(), getMaxValue(), value);
    }

    protected int getCalculateValuePercentageOld(double min, double max, double value) {
        if (min >= value)
            return 0;
        if (max <= value)
            return 100;
        return (int) ((value - min) / (max - min) * 100);
    }

    protected int getCalculateValuePercentage(double min, double max, double value) {
        if (min < 0 && max < 0 && min < max) {
            return getCalculateValuePercentageUseCaseOne(min, max, value);
        } else if (min < 0 && max < 0 && min > max) {
            return getCalculateValuePercentageUseCaseTwo(min, max, value);
        } else if ((min >= 0 && max < 0) || (min < 0 && max >= 0)) {
            if (min > max) {
                return getCalculateValuePercentageUseCaseThree(min, max, value);
            } else if (min < max) {
                return getCalculateValuePercentageUseCaseFoure(min, max, value);
            }
        }
        return getCalculateValuePercentageOld(min, max, value);
    }

    /**
     * Use case when min and max negative
     * and min smaller than max
     */
    private int getCalculateValuePercentageUseCaseOne(double min, double max, double value) {
        if (value <= Math.min(min, max))
            return 0;
        if (value >= Math.max(min, max))
            return 100;
        else {
            double available = Math.abs(Math.min(min, max)) - Math.abs(Math.max(min, max));
            double minValue = Math.min(min, max);
            double result = Math.abs(((minValue - value) / (available) * 100));
            return (int) result;
        }
    }

    /**
     * Use Case when min and max negative
     * and min bigger than max
     */
    private int getCalculateValuePercentageUseCaseTwo(double min, double max, double value) {
        if (value <= Math.min(min, max))
            return 100;
        if (value >= Math.max(min, max))
            return 0;
        else {
            double available = Math.abs(Math.min(min, max)) - Math.abs(Math.max(min, max));
            double maxValue = Math.max(min, max);
            double result = Math.abs(((maxValue - value) / (available) * 100));
            return (int) result;
        }
    }


    //TODO: Need improvements for calculation algorithms for next to methods

    /**
     * Use case when one of the limits are negative and one is positive
     * TODO: Need Improvements
     */
    private int getCalculateValuePercentageUseCaseThree(double min, double max, double value) {
        double available = Math.abs(min) + Math.abs(max);
        if (value <= Math.min(min, max)) {
            return 100;
        } else if (value >= Math.max(min, max))
            return 0;
        else{
            double positive = Math.max(min, max);
            double result = Math.abs((positive - value) / (available) * 100);
            return (int) result;
        }
    }

    /**
     * Use case when one of the limits are negative and one is positive
     * and max is bigger than min
     * TODO: Need Improvements
     */
    private int getCalculateValuePercentageUseCaseFoure(double min, double max, double value) {
        double available = Math.abs(min) + Math.abs(max);
        if (value <= Math.min(min, max)) {
            return 0;
        } else if (value >= Math.max(min, max))
            return 100;
        else{
            double negative = Math.abs(Math.min(min, max));
            double result = Math.abs((negative + value) / (available) * 100);
            return (int) result;
        }
    }

    public int getTotalValue() {
        return Math.abs(getMinValue()) + getMaxValue();
    }

    private float[] CalculateValueRanges() {
        //divisions = mDivisions + 1 //division tick 그릴때 + 1 해서 그리기때문에  +1 추가
        int divisions = mDivisions + 1;
        float[] arr = new float[divisions];
        for(int i = 0; i < divisions ; i++) {
            //처음
            if (i == 0) {
                arr[i] = minValue;
            }
            //마지막
            else if (i + 1 == divisions) {
                arr[i] = maxValue;
            }
            else {
                float num = ((getTotalValue() / (mDivisions)) * (i)) + minValue ;
                int num_length = (int)(Math.log10(Math.abs(num)) + 1); //숫자 자리수 확인
                float roundnum = num_length > 2 ? (float) 0.1 : (float) 1.0; //백자리 이상이면 0.1 외에는 1.0
                roundnum = (int)(Math.log10(Math.abs(maxValue)) + 1) > 2 ? roundnum : (float) 0.1; // 맥
                for (int j = 1; j < num_length ; j++) {
                    roundnum *= 10.0;
                }
                num = Math.round(num /roundnum) * roundnum;
                arr[i] = num;
            }
        }

        return arr;
    }
    public void initGaugeView(int min, int max) {
        try {
            if (min >= max) {
                throw new Error("min값이 max값보다 높을수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setMinValue(min);
        setMaxValue(max);
        setRangeValues(CalculateValueRanges());
        setTargetValue(min);
    }
    public void addRanges(int min, int max, int color){
        GaugeRange gr = new GaugeRange();
        gr.setMinValue(min);
        gr.setMaxValue(max);
        gr.setColor(color);
        addGaugeRange(gr);
    }
}
