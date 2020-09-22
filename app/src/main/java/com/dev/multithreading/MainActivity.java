package com.dev.multithreading;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private TextView textView;
    private Runnable runnable;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        handler = new UIHandler();
    }

    private void initViews() {
        textView = findViewById(R.id.textview);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button) {
            Log.e("TAG", "Thread : " + Thread.currentThread().getName());
            //prepareRunnable();

            //runnable.run();

            //prepareCounterRunnable();
            //Thread thread = new Thread(runnable);
            //thread.start();
            //thread.run();

            AsyncTaskCounter asyncTaskCounter = new AsyncTaskCounter();
            asyncTaskCounter.execute(10);
        }
    }

    private void prepareRunnable() {
        //final Handler handler = new Handler();

        /*
        runnable = new Runnable() {
                @Override
                public void run() {
                    Log.e(TAG, "Thread : " + Thread.currentThread().getName());
                    for (int i = 0; i < 10; i++) {
                        SystemClock.sleep(1000);
                        Log.e(TAG, "i = " + i);
                    }
                }
            };
         */

        runnable = new Runnable() {
            @Override
            public void run() {
                Log.e("TAG", "Thread : " + Thread.currentThread().getName());
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    Log.getStackTraceString(e);
                }
                //Log.e("TAG", "Vous avez commencé ...");
                //textView.setText("Vous avez cliqué sur Commencer !");

                /*
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "Vous avez commencé ...");
                        textView.setText("Vous avez cliqué sur Commencer !");
                    }
                });

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "Après 3 secondes ...");
                        textView.setText("Message après 3 secondes !");
                    }
                }, 3000);
                */

                handler.obtainMessage(1).sendToTarget();
                handler.obtainMessage(2).sendToTarget();
            }
        };
    }

    private void prepareCounterRunnable() {
        final Handler handler = new Handler();

        runnable = new Runnable() {
            int counter = 10;

            @Override
            public void run() {
                while (counter != 0) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("TAG", "Veuillez patienter " + counter + " secondes...");
                            textView.setText("Veuillez patienter " + counter + " secondes...");
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        Log.getStackTraceString(e);
                    }

                    counter--;
                }

                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("TAG", "Merci. Bienvenue !");
                        textView.setText("Merci. Bienvenue !");
                    }
                });
            }
        };
    }

    class UIHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    Log.e("TAG", "Vous avez commencé ...");
                    textView.setText("Vous avez commencé ...");
                    break;
                }
                case 2: {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("TAG", "Après 3 secondes ...");
                            textView.setText("Message après 3 secondes !");
                        }
                    }, 3000);
                    break;
                }
            }
        }
    }

    class AsyncTaskCounter extends AsyncTask<Integer, Integer, String> {

        @Override
        protected void onPreExecute() {
            Log.e(TAG, "Vous avez commencé ...");
            textView.setText("Vous avez commencé ...");
        }

        @Override
        protected String doInBackground(Integer... integers) {
            Log.e(TAG, "Thread : " + Thread.currentThread().getName());
            int counter = integers[0];
            while (counter != 0) {

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Log.getStackTraceString(e);
                    return "Une erreur s'est produite!";
                }

                Log.e("TAG", "Veuillez patienter " + counter + " secondes...");
                publishProgress(counter);

                counter--;
            }

            return "Merci. Bienvenue !";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            textView.setText("Veuillez patienter " + values[0] + " secondes...");
        }

        @Override
        protected void onPostExecute(String s) {
            textView.setText(s);
        }
    }
}
