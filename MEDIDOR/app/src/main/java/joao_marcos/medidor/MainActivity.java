package joao_marcos.medidor;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private TextView temperaturelabel;
    private SensorManager mSensorManager;
    private Sensor mTemperature;
    private final static String NOT_SUPPORTED_MESSAGE = "Sorry, sensor not available for this device.";


    TextView mStatusView;
    MediaRecorder mRecorder;
    Thread runner;
    private static double mEMA = 0.0;
    static final private double EMA_FILTER = 0.6;

    BarChart barChart;

    ArrayList<String> valores = new ArrayList<>();

    String IP, mensg;
    private Button salvaIP;
    private EditText ip;
    private Button atualizarGrafico;


    final Runnable updater = new Runnable(){

        public void run(){
            updateTv();
        };
    };

    final Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        salvaIP = (Button) findViewById(R.id.button_salvaIP);
        ip = (EditText) findViewById(R.id.edt_ip);
        atualizarGrafico = (Button) findViewById(R.id.btn_atualizargrafico);
        barChart = (BarChart) findViewById(R.id.grafico);




        atualizarGrafico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                ArrayList<BarEntry> entries = new ArrayList<>();
                //entries.add(new BarEntry(0, 0));

                //entries.add(new BarEntry(1, 2));
                //entries.add(new CandleEntry(1, (float) varianciaAPP02, (float) (varianciaAPP02 - (0.90 * varianciaAPP02)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.70)), (float) (varianciaAPP02 - (varianciaAPP02 * 0.30))));

                for (int i = 0; i < valores.size(); i++){
                    entries.add(new BarEntry(Float.parseFloat(valores.get(i)), i));
                }

                BarDataSet dataSet= new BarDataSet (entries, "Histórico Decibéis");

                ArrayList<String> labels = new ArrayList<>();

                for (int i = 0; i < valores.size(); i++){
                    labels.add("seg"+i);
                }

                //labels.add(nomesAPPS.get(1));

                BarData data = new BarData(labels, dataSet);
                barChart.setData(data);
                dataSet.setColors(ColorTemplate.COLORFUL_COLORS); //
                barChart.setDescription("Variações");
                barChart.animateY(2000);
                barChart.invalidate();
            }
        });




        salvaIP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IP = ip.getText().toString();
                Toast.makeText(MainActivity.this, "IP Salvo: "+IP, Toast.LENGTH_SHORT).show();


                //INICIANDO SERVER SOCKET DA APLICAÇÃO
/*
                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {

                        try {

                            ServerSocket ss = new ServerSocket(9002);

                            while (true) {
                                //Server is waiting for client here, if needed
                                Socket s = ss.accept();
                                BufferedReader input = new BufferedReader(new InputStreamReader(s.getInputStream()));

                                mensg = input.readLine();

                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (!mensg.equalsIgnoreCase("")){
                                    Toast.makeText(MainActivity.this, "RECEBEU COMANDO!", Toast.LENGTH_SHORT).show();

                                }
                                s.close();
                            }
                            //ss.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "Erro na criação do servidor!", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
                thread.start();

*/


                //Envio dos dados coletados para o Cliente Java


                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Thread thread = new Thread(new Runnable() {
                            @Override
                            public void run() {

                                    try {


                                        for(;;) {
                                            try {
                                                //while(true) {
                                                //Adicionando o IP configurável aqui

                                                Socket s = new Socket(IP, 9002);

                                                OutputStream out = s.getOutputStream();

                                                PrintWriter output = new PrintWriter(out);

                                                output.println(mStatusView.getText().toString());
                                                output.flush();

                                                //valores.add(Float.parseFloat(mStatusView.getText().toString()));
                                                valores.add(mStatusView.getText().toString());
                                                Thread.sleep(3000);
                                            }catch(InterruptedException a){
                                                Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT).show();
                                            }
                                        }

                                    } catch (IOException ver) {
                                        Toast.makeText(MainActivity.this, "Erro no envio da mensagem", Toast.LENGTH_SHORT).show();
                                    }
                            }

                        });
                        thread.start();





                    }
                });






            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


/*
        Medição de Tempertura

        temperaturelabel = (TextView) findViewById(R.id.myTemp);
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.ICE_CREAM_SANDWICH){
            mTemperature= mSensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE); // requires API level 14.
        }
        if (mTemperature == null) {
            temperaturelabel.setText(NOT_SUPPORTED_MESSAGE);
            Toast.makeText(this, "Não Há sensor de temperatura!", Toast.LENGTH_SHORT).show();
        }

*/



        //Medição de Áudio


        mStatusView = (TextView) findViewById(R.id.status);

        if (runner == null)
        {
            runner = new Thread(){
                public void run()
                {
                    while (runner != null)
                    {
                        try
                        {
                            Thread.sleep(1000);
                            Log.i("Noise", "Tock");
                        } catch (InterruptedException e) { };
                        mHandler.post(updater);
                    }
                }
            };
            runner.start();
            Log.d("Noise", "start runner()");
        }



        //Chamando o método para iniciar gravação aqui
        if (Build.VERSION.SDK_INT >= 23 && (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED)) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.INTERNET, android.Manifest.permission.ACCESS_WIFI_STATE,
                    android.Manifest.permission.READ_PHONE_STATE, android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    android.Manifest.permission.WAKE_LOCK, Manifest.permission.RECORD_AUDIO
            }, 0);
        }
        startRecorder();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.temepratura) {
            // Handle the camera action
        } else if (id == R.id.configurar) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


/*
    public void onSensorChanged(SensorEvent event) {
        float ambient_temperature = event.values[0];
        //temperaturelabel.setText("Ambient Temperature:\n " + String.valueOf(ambient_temperature) + getResources().getString(R.string.celsius));
        temperaturelabel.setText("Ambient Temperature:\n " + String.valueOf(ambient_temperature));
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Do something here if sensor accuracy changes.
    }

*/



    //MEDIÇÃO DE ÁUDIO

/*

    public void onResume()
    {
        super.onResume();
        startRecorder();
    }

    public void onPause()
    {
        super.onPause();
        stopRecorder();
    }


*/



    public void startRecorder(){
        if (mRecorder == null)
        {
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile("/dev/null");
            try
            {
                mRecorder.prepare();
            }catch (java.io.IOException ioe) {
                android.util.Log.e("[Monkey]", "IOException: " +
                        android.util.Log.getStackTraceString(ioe));

            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }
            try
            {
                mRecorder.start();
            }catch (java.lang.SecurityException e) {
                android.util.Log.e("[Monkey]", "SecurityException: " +
                        android.util.Log.getStackTraceString(e));
            }

            //mEMA = 0.0;
        }

    }
    public void stopRecorder() {
        if (mRecorder != null) {
            mRecorder.stop();
            mRecorder.release();
            mRecorder = null;
        }
    }

    public void updateTv(){
        //mStatusView.setText(Double.toString((getAmplitudeEMA())) + " dB");
        mStatusView.setText(Double.toString((getAmplitudeEMA())));
    }
    public double soundDb(double ampl){
        return  20 * Math.log10(getAmplitudeEMA() / ampl);
    }
    public double getAmplitude() {
        if (mRecorder != null)
            return  (mRecorder.getMaxAmplitude());
        else
            return 0;

    }
    public double getAmplitudeEMA() {
        double amp =  getAmplitude();
        mEMA = EMA_FILTER * amp + (1.0 - EMA_FILTER) * mEMA;
        return mEMA;
    }




}
