package custom.android.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.gvtest.R;

public class TempGauge  extends LinearLayout {

    private LinearLayout tempGauge;
    private TextView valuetext;
    private TextView nametext;
    private String unittext;
    private SeekBar seekBar;
    private TempBar tempBar;
    private ValueAnimator.AnimatorUpdateListener aniListner;
    private AnimationUtils animationUtils = new AnimationUtils();

    private int curValue = 0;
    private int targetValue = 0;


    public TempGauge(Context context) {
        super(context);
        initView();
    }

    public TempGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public TempGauge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs);
        initView();
        getAttrs(attrs, defStyle);
    }

    private void initView() {

        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.custom_view_tempgauge, this, false);
        addView(v);

        tempGauge = (LinearLayout) findViewById(R.id.tempGauge);
        valuetext = (TextView) findViewById(R.id.valueText);
        nametext = (TextView) findViewById(R.id.nameText);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        tempBar = (TempBar) findViewById(R.id.tempBar);

        //애니메이션 리스너 설정
        aniListner = new ValueAnimator.AnimatorUpdateListener()
        {
            public void onAnimationUpdate(ValueAnimator animation)
            {
                int currentValue = (int) animation.getAnimatedValue();
                curValue = currentValue;
                valuetext.setText(Integer.toString(currentValue) + unittext);
                seekBar.setProgress(tempBar.getCalculateValuePercentage(currentValue));
            }
        };

        //seekbar 터치불가
        seekBar.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TempGauge);
        setTypeArray(typedArray);
    }
    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.TempGauge, defStyle, 0);
        setTypeArray(typedArray);

    }
    private void setTypeArray(TypedArray typedArray) {

        String value = typedArray.getString(R.styleable.TempGauge_valuetext);
        valuetext.setText(value);

        String name  = typedArray.getString(R.styleable.TempGauge_nametext);
        nametext.setText(name);

        String unit = typedArray.getString(R.styleable.TempGauge_unittext);
        unittext = unit;

        if(unittext == null || unittext.isEmpty()) {
            unittext = "°"; //기본값 온도 단위 설정
        }
        typedArray.recycle();
    }

    public void initTempGauge(int min, int max) {
        tempBar.initTempBar(min, max);
    }
    public void addRanges(int min, int max, int color) {
        tempBar.addRanges(min, max, color);
    }
    public void setValueText(String str) {
        //valuetext.setText(str);
        // seekbar animation 및 수정 애니메이션 추가하기
        animationUtils.refresh(aniListner, curValue, Integer.parseInt(str));
    }
    public void setNameText(String str) {
        nametext.setText(str);
    }

}
