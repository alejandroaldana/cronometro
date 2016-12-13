package com.example.keev.cronometrofull;

import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /* Definir Botones y TextView */
    Button btnStart, btnPause, btnLap, btnRestart;
    TextView txtReloj;
    /**/

    Handler customHandler = new Handler();
    LinearLayout container;
    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMilliseconds;
            int secs = (int) (updateTime/1000);
            int mins = (secs/60);
            int milliseconds = (int) (updateTime%1000);
            txtReloj.setText("" + mins + ":" + String.format("%02d", secs) + ":" + String.format("%03d", milliseconds));
            customHandler.postDelayed(this, 0);
        }
    };

    long startTime = 0L,
         timeInMilliseconds = 0L,
         timeSwapBuff = 0L,
         updateTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Inicializar Botones y textView */
        btnStart = (Button) findViewById(R.id.btnStart);
        btnPause = (Button) findViewById(R.id.btnPause);
        btnLap = (Button) findViewById(R.id.btnLap);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        txtReloj = (TextView) findViewById(R.id.reloj);
        /**/

        container = (LinearLayout) findViewById(R.id.container);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Poner el tiempo inicial */
                startTime = SystemClock.uptimeMillis();
                /**/

                customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Poner el tiempo de pausa */
                timeSwapBuff += timeInMilliseconds;
                /**/

                customHandler.removeCallbacks(updateTimerThread);
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = inflater.inflate(R.layout.row, null);
                TextView txtValue = (TextView) addView.findViewById(R.id.txtContent);

                /* Poner texto */
                txtValue.setText(txtReloj.getText());
                /**/

                container.addView(addView);
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customHandler.removeCallbacks(updateTimerThread);

                /* Poner valores a cero */
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                /**/

                txtReloj.setText("0:00:000");
                container.removeAllViews();
            }
        });
    }
}
