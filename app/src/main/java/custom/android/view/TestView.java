package custom.android.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.gvtest.R;

public class TestView extends View {
    private Paint pnt;
    private String Title_Text; // 타이틀 텍스트
    private String Unit_Text; //단위 텍스트
    private int Main_Divisions; //
    private int Sub_Divisions;
    private float MIN_VAL; //최소값
    private float MAX_VAL; //최대값
    private float Current_value; //현재 값
    private float Target_value; //목표 값

    public static final float TOP = 0.0f;
    public static final float LEFT = 0.0f;
    public static final float RIGHT = 1.0f;
    public static final float BOTTOM = 1.0f;
    public static final float CENTER = 0.5f;
    private static final int SCALE_DIVISIONS = 6;
    private static final int SCALE_SUBDIVISIONS = 1;

    private RectF BASIC_RECT;
    private Bitmap Background;

    public TestView(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        //readAttrs(context, attrs, defStyle);
        init();
    }

    public TestView(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TestView(final Context context) {
        this(context, null, 0);
    }

    private void init() {
        // TODO Why isn't this working with HA layer?
        // The needle is not displayed although the onDraw() is being triggered by invalidate()
        // calls.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initDrawingRects();

    }
    public void initDrawingRects() {
        BASIC_RECT = new RectF(LEFT, TOP, RIGHT, BOTTOM);
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        drawBackground(canvas);
        drawCircle(canvas);
    }

    private void readAttrs(final Context context, final AttributeSet attrs, final int defStyle) {
        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.GaugeView, defStyle, 0);

        Main_Divisions = a.getInteger(R.styleable.GaugeView_divisions, SCALE_DIVISIONS);
        Sub_Divisions = a.getInteger(R.styleable.GaugeView_subdivisions, SCALE_SUBDIVISIONS);

        /*if (mShowRanges) {
            mTextShadowColor = a.getColor(R.styleable.GaugeView_textShadowColor, TEXT_SHADOW_COLOR);

            final CharSequence[] rangeValues = a.getTextArray(R.styleable.GaugeView_rangeValues);

            final CharSequence[] rangeColors = a.getTextArray(R.styleable.GaugeView_rangeColors);
            readRanges(rangeValues, rangeColors);
            mShowRangeValues = DEFAULT_SHOW_RANGE_VALUES;
        }*/

        a.recycle();
    }

    private void drawBackground(Canvas canvas) {
        if (null != Background) {
            Background.recycle();
        }
        // Create a new background according to the new width and height
        Background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(Background);
        canvas.drawColor(Color.RED); //바탕색
        //canvas.drawColor(Color.rgb(30,41,54)); //바탕색
        final float scale = Math.min(getWidth(), getHeight());
        canvas.scale(scale, scale);
        canvas.translate((scale == getHeight()) ? ((getWidth() - scale) / 2) / scale : 0
                , (scale == getWidth()) ? ((getHeight() - scale) / 2) / scale : 0);

        RectF rf = new RectF();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        rf.set(0,0,getWidth(),getHeight());
        canvas.drawRect(rf, paint);

    }

    private void drawCircle(Canvas canvas) {
        Paint paint = new Paint();

        RectF rf = new RectF();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);
        rf.set(0,0,getWidth(),getHeight());
        canvas.drawRect(rf, paint);

        // 크레파스의 색 정하기
        paint.setAntiAlias(true);
        paint.setColor(Color.rgb(47,56,68));
        paint.setStyle(Paint.Style.STROKE); // 스타일 설정
        paint.setStrokeWidth(15); //선 굵기

        // 원 그리기
        canvas.drawCircle(getWidth() / 2, getHeight() /2, 150, paint);

        paint.setColor(Color.rgb(0,172,172));
        paint.setStyle(Paint.Style.FILL); // 스타일 설정

        canvas.drawCircle(getWidth() /2 , (getHeight() / 2) + 150 , 15 , paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        canvas.drawCircle(getWidth() /2 , (getHeight() / 2) + 150 , 20 , paint);
        //canvas.drawCircle();
        //canvas.drawOval(rect ,paint);
        //canvas.drawArc(rect, 0, 360, true, paint);

        invalidate();
    }
}
