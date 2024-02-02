package com.example.gvtest;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import custom.android.view.GaugeView;
import custom.android.view.TempGauge;

public class MainActivity extends AppCompatActivity {

    private final int GAUGE_YELLOW = Color.parseColor("#E7B10A");
    private final int GAUGE_GREEN = Color.parseColor("#82B528");
    private final int GAUGE_RED = Color.parseColor("#E31C25");
    private final int GAUGE_BLUE = Color.parseColor("#0487FF");
    private final int GAUGE_DARK = Color.parseColor("#000000");

    private final int TEMP_YELLOW = Color.parseColor("#F9AD4E");
    private final int TEMP_GREEN = Color.parseColor("#70CBC1");
    private final int TEMP_ORANGE = Color.parseColor("#FB5F48"); //주황
    private final int TEMP_BLUE = Color.parseColor("#4F8ED8");

    private TempGauge tempgauge;
    private TempGauge tempgauge1;
    private TempGauge tg3;
    final Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //aa
        final GaugeView gaugeView = (GaugeView) findViewById(R.id.gaugeView);
        gaugeView.initGaugeView(0, 1500);  //게이지뷰 최소 최대값 설정
        gaugeView.addRanges(0,400, GAUGE_YELLOW); //해당 범위 게이지 색 설정
        gaugeView.addRanges(400,700, GAUGE_GREEN);
        gaugeView.addRanges(700,1500, GAUGE_YELLOW); //뒤에 추가한게 위로 덧칠됨

        final GaugeView gv2 = (GaugeView) findViewById(R.id.gv2);
        gv2.initGaugeView(0, 1000);
        gv2.addRanges(0,400, GAUGE_BLUE);
        gv2.addRanges(400,700, GAUGE_YELLOW);
        gv2.addRanges(700,1000, GAUGE_GREEN);

        final GaugeView gv3 = (GaugeView) findViewById(R.id.gv3);
        gv3.initGaugeView(-1000,2000);
        gv3.addRanges(-1000,700, GAUGE_GREEN);
        gv3.addRanges(700,1100, GAUGE_BLUE);
        gv3.addRanges(1100,2000, GAUGE_RED);

        final GaugeView gv4 = (GaugeView) findViewById(R.id.gv);
        gv4.initGaugeView(-1000,2000);
        gv4.addRanges(-1000,700, GAUGE_GREEN);
        gv4.addRanges(700,1100, GAUGE_BLUE);
        gv4.addRanges(1100,2000, GAUGE_RED);

        tempgauge = (TempGauge) findViewById(R.id.tg1); //커스텀온도게이지 뷰 연결
        tempgauge.initTempGauge(0, 1000); //base //최소, 최대값 설정 - default = 0,100
        tempgauge.addRanges(0,400,TEMP_YELLOW); //배경색 코드값이 투명이라서 추가하지 않을경우 비어있는것처럼 보임
        tempgauge.addRanges(400,550,TEMP_GREEN);
        tempgauge.addRanges(550,900,TEMP_YELLOW);
        tempgauge.addRanges(900,1000,TEMP_ORANGE);

        tempgauge1 = (TempGauge) findViewById(R.id.tg2);
        tempgauge1.initTempGauge(-40, 100); //base
        tempgauge1.addRanges(-40,10,TEMP_GREEN);
        tempgauge1.addRanges(10,30,TEMP_YELLOW);
        tempgauge1.addRanges(30,50,TEMP_BLUE);
        tempgauge1.addRanges(50,100,TEMP_ORANGE);
        //

        tg3 = (TempGauge) findViewById(R.id.tg3);
        tg3.initTempGauge(0, 100);
        tg3.addRanges(0, 10, TEMP_YELLOW);
        tg3.addRanges(10, 80, TEMP_GREEN);
        tg3.addRanges(80, 100, TEMP_BLUE);


        Timer mTimer = new Timer();
        // 코드 실행을 위한 TimerTask 생성
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //gaugeView.setTargetValue((random.nextInt(gaugeView.getTotalValue())) + gaugeView.getMinValue());
                //gv2.setTargetValue((random.nextInt(gv2.getTotalValue())) + gv2.getMinValue());
                //gv3.setTargetValue((random.nextInt(gv3.getTotalValue())) + gv3.getMinValue());
                //gv4.setTargetValue((random.nextInt(gv4.getTotalValue())) + gv4.getMinValue());

                Message msg = animationhandler.obtainMessage();
                animationhandler.sendMessage(msg);
            }
        };
        // 0.1초마다 task를 실행
        mTimer.schedule(task, 0, 5000);


    }
    //밸류 애니메이터가 적용되어있어 핸들러를 통해 valuetext 수정
    private Handler animationhandler = new Handler(){
        //애니메이션
        public void handleMessage(Message msg){
            tempgauge.setValueText(Integer.toString(random.nextInt(1000) ));  //밸류 텍스트 변경
            tempgauge1.setValueText(Integer.toString(random.nextInt(140 ) - 40));
            tg3.setValueText(Integer.toString(random.nextInt(100 )));

        }
    };
}

