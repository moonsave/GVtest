package custom.android.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class TempBar extends View {

    private int MIN_VALUE = 0;
    private int MAX_VALUE = 100;

    private static final float TOP = 0.0f;
    private static final float LEFT = 0.0f;
    private static final float RIGHT = 1.0f;
    private static final float BOTTOM = 1.0f;
    private static final float CENTER = 0.5f;

    private RectF BG_RECT; //배경
    private RectF BG_ROUND_RECT; //progress 배경
    private RectF RANGE_ROUND_RECT; //progress 채우기

    private Paint BG_RECT_PAINT;
    private Paint BG_ROUND_RECT_PAINT;

    private List<GaugeRange> gaugeRangeList = new ArrayList<>();

    public TempBar(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        //readAttrs(context, attrs, defStyle);
        init();
    }

    public TempBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TempBar(final Context context) {
        this(context, null, 0);
    }


    private void init() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        initDrawingRects(); //rect 설정
        initDrawingTools(); //페인트 설정
    }
    public void initTempBar(int min, int max) {
        try {
            if (min >= max) {
                throw new Error("min값이 max값보다 높을수 없습니다.");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        setMinValue(min);
        setMaxValue(max);
    }
    private void initDrawingRects() {

        BG_RECT = new RectF(LEFT, TOP, RIGHT, BOTTOM);

        float widthMagin = 0.1f;
        float heightMagin = 0.15f;
        //배경 *정사각형 배경 위치 설정

        //round_rect 배경 위치 설정 *타원형 progress배경
        BG_ROUND_RECT = new RectF(BG_RECT.left + widthMagin, BG_RECT.top + heightMagin, BG_RECT.right - widthMagin, BG_RECT.bottom - heightMagin);

        //round_rect range 위치 설정
        RANGE_ROUND_RECT = new RectF(BG_ROUND_RECT.left, BG_ROUND_RECT.top, BG_ROUND_RECT.right, BG_ROUND_RECT.bottom);
    }
    private void initDrawingTools() {
        BG_RECT_PAINT = getBgRectPaint();
        BG_ROUND_RECT_PAINT = getBgRoundRectPaint();
    }
    private Paint getBgRectPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.parseColor("#00FFFFFF")); //배경색
        return paint;
    }
    private Paint getBgRoundRectPaint() {
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL); // 스타일 설정
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setColor(Color.parseColor("#00000000"));
        return paint;
    }

    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.scale(getWidth(), getHeight());

        drawBackground(canvas);
        drawBackProgress(canvas);
        drawGaugeRanges(canvas);
    }


    private void drawBackground(Canvas canvas) {
        canvas.save();
        canvas.drawRect(BG_RECT,  BG_RECT_PAINT);
    }

    private void drawBackProgress(Canvas canvas) {
        //프로그레스 배경
        canvas.save();
        canvas.drawOval(BG_ROUND_RECT, BG_ROUND_RECT_PAINT);
    }

    private void drawGaugeRanges(Canvas canvas) {
        canvas.save();
        int count = gaugeRangeList.size(); //온도바 칸 개수
        if (count < 1) { //0개면 리턴
            return;
        }

        for (int i = 0 ; i < count ; i++) {
            GaugeRange range = gaugeRangeList.get(i);
            int min = range.getMinValue();
            int max = range.getMaxValue();
            float rectNum = 1.5f;
            int totalNum = (Math.abs(getMinValue()) + getMaxValue()) / 10;

            if(min <= getMinValue()) {
                //처음
                //최소값이 음수일때 계산식이 달라짐 값세팅에따라 온도바 그려지는게 달라지므로 확인 필요
                int ovalmax = (int) (min < 0 ?  (totalNum * 2  + min) : (totalNum * 2  - min));
                canvas.drawOval(getRangeRoundRect(
                        min ,
                        ovalmax ),
                        getRangePaint(range.getColor()));

                canvas.drawRect(getRangeRoundRect(
                        (int) (min < 0 ?  -(Math.abs(ovalmax) * 2) : (ovalmax / 2)) ,
                        max) ,
                        getRangePaint(range.getColor()));
            }
            if (max >= getMaxValue()) {
                //마지막
                canvas.drawOval(getRangeRoundRect(
                        (int) (max * 0.8),
                        max ),
                        getRangePaint(range.getColor()));
                canvas.drawRect(getRangeRoundRect(
                        min,
                        (int) (max - (totalNum / rectNum))),
                        getRangePaint(range.getColor()));
            }

            if (min > getMinValue() &&
                max < getMaxValue() ) {
                canvas.drawRect(getRangeRoundRect(min, max) ,getRangePaint(range.getColor()));
            }
            //내용 추가 //
            if( i > 0) {
                for (GaugeRange prevRange : gaugeRangeList) {
                    int prevMin = prevRange.getMinValue();
                    int prevMax = prevRange.getMaxValue();
                    int lastoval = (int) (getMaxValue() * 0.8);
                    if (prevMax >= lastoval && prevMax != getMaxValue()) {
                        canvas.drawRect(getRangeRoundRect(prevMin, prevMax) ,getRangePaint(prevRange.getColor()));
                    }
                }
            }
        }

        //invalidate();
    }
    protected Paint getRangePaint(int color) {
        Paint range = new Paint(Paint.ANTI_ALIAS_FLAG);
        range.setColor(color);
        range.setStyle(Paint.Style.FILL);
        range.setStrokeCap(Paint.Cap.ROUND);
        return range;
    }

    public void setMinValue(int value) {
        this.MIN_VALUE = value;
    }
    public int getMinValue() { return this.MIN_VALUE; }
    public void setMaxValue(int value) {
        this.MAX_VALUE = value;
    }
    public int getMaxValue() { return this.MAX_VALUE; }
    public List<GaugeRange> getGaugeRanges() {
        return gaugeRangeList;
    }

    public void addGaugeRange(GaugeRange range) {
        if (range == null)
            return;
        gaugeRangeList.add(range);
    }
    public void addRanges(int min, int max, int color){
        GaugeRange gr = new GaugeRange();
        gr.setMinValue(min);
        gr.setMaxValue(max);
        gr.setColor(color);
        addGaugeRange(gr);
    }
    private RectF getRangeRoundRect(int min, int max) {
        //left, top 값은 0.1 ~ 0.9값이 나오는 함수 사용
        RANGE_ROUND_RECT.set(calculateRange(min),
                BG_ROUND_RECT.top ,
                calculateRange(max),
                BG_ROUND_RECT.bottom );
        return RANGE_ROUND_RECT;
    }
    private float calculateRange(double from) {
        //최소0.1 ~ 최대 0.9 사이의 값이 나오도록 계산
        //return ((0.8f / 100.0f * getCalculateValuePercentage(from)) + 0.1f);
        // 0.05 ~ 0.95
        return ((0.9f / 100.0f * getCalculateValuePercentage(from)) + 0.05f);
        // 2 ~ 8
        //return ((0.6f / 100.0f * getCalculateValuePercentage(from)) + 0.2f);
    }

    /**퍼센티지 계산식 함수
     * */
    protected int getCalculateValuePercentage(double value) {
        return getCalculateValuePercentage(getMinValue(), getMaxValue(), value);
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
    protected int getCalculateValuePercentageOld(double min, double max, double value) {
        if (min >= value)
            return 0;
        if (max <= value)
            return 100;
        return (int) ((value - min) / (max - min) * 100);
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

}
