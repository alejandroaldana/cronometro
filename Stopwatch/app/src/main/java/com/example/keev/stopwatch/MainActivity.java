
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

    Button btnStart, btnPause, btnLap, btnRestart;
    TextView txtReloj;

    Handler customHandler = new Handler();
    LinearLayout container;
    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            //el tiempo que ha pasado desde startTime
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            //lo sumamos al que ya teniamos
            updateTime = timeSwapBuff + timeInMilliseconds;
            //calculamos los segundos, minutos y milisegundos
            int secs = (int) (updateTime/1000);
            int mins = (secs/60);
            int milliseconds = (int) (updateTime%1000);
            //lo ponemos desntro del cuadro de texto
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

        /* Inicializar Boton start */
        
        /**/
        
        btnPause = (Button) findViewById(R.id.btnPause);
        btnLap = (Button) findViewById(R.id.btnLap);
        btnRestart = (Button) findViewById(R.id.btnRestart);
        txtReloj = (TextView) findViewById(R.id.reloj);
        container = (LinearLayout) findViewById(R.id.container);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /* Poner en startTime el tiempo en este momento */
                
                /**/

                customHandler.postDelayed(updateTimerThread, 0);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSwapBuff += timeInMilliseconds;
                customHandler.removeCallbacks(updateTimerThread);
            }
        });

        btnLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View addView = inflater.inflate(R.layout.row, null);
                TextView txtValue = (TextView) addView.findViewById(R.id.txtContent);
                txtValue.setText(txtReloj.getText());
                container.addView(addView);
            }
        });

        btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                customHandler.removeCallbacks(updateTimerThread);
                startTime = 0L;
                timeInMilliseconds = 0L;
                timeSwapBuff = 0L;
                updateTime = 0L;
                txtReloj.setText("0:00:000");
                container.removeAllViews();
            }
        });
    }
    
    public static long tiempoEnEsteMomento() {
        return SystemClock.uptimeMillis();
    }
}
