package www.shiftcntrlalt.com.counter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.content.ServiceConnection;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import www.shiftcntrlalt.com.counter.MyService.MyLocalBinder;

public class MainActivity extends Activity {

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            MyLocalBinder myBinder = (MyLocalBinder) service;
            myService = myBinder.getService();
            Log.i(TAG, "service connection done");
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
            myService = null;
            Log.i(TAG, "Service Disconnected");

        }
    };


    private boolean isBound = false;
    MyService myService;
    private static final String TAG = "myTAG";
    public TextView myTextview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTextview = (TextView) findViewById(R.id.myTextView);
        Button start = (Button) findViewById(R.id.startButton);
        Button stop = (Button) findViewById(R.id.stopButton);
        myTextview.setText("0");


        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(v.getContext(), "Click again", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(v.getContext(), MyService.class);
                bindService(i, serviceConnection, Context.BIND_AUTO_CREATE);
                Log.i(TAG, "Start Button CLicked");

                if (isBound) {

                    //Toast.makeText(v.getContext(), myService.getTime(), Toast.LENGTH_LONG).show();
                    Log.i(TAG,"entered if(isBound) statement");
                    myTextview.setText(String.valueOf(myService.getValue()));
                    sleeping();
                }


            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myTextview.setText("0");
                Log.i(TAG, "Stop button clicked");
                try {
                    if (isBound) {
                        unbindService(serviceConnection);
                        isBound = false;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "Error msg caught", e);
                }
            }
        });


    }

    public void sleeping() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isBound) {
                    try {
                        Thread.sleep(3600000);
                        //Thread.sleep(60000);
                        if (isBound) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    myService.start();
                                    myTextview.setText(String.valueOf(myService.getValue()));
                                }
                            });

                        }
                    } catch (InterruptedException e) {
                        Log.i(TAG, "main thread stopped");
                    }
                }

            }
        }).start();


    }
}






