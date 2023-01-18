package com.example.thread;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int MESSAGE_PROGRESS = 0;
    private static final int MESSAGE_DONE = 1;
    ProgressBar progressBar;
    TextView messageView;

    //timeout dialog
    private static final int MESSAGE_DIALOG_TIMEOUT = 1;
    private static final long TIMEOUT_DIALOG_DELAY = 5000;
    AlertDialog mDialog;



    Handler mDialogHandler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MESSAGE_DIALOG_TIMEOUT:
                    mDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Dialog Timeout", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    //back key 처리
    private static final int MESSAGE_BACKKEY_TIMEOUT = 2;
    private static final long TIMEOUT_BACKKEY_DELAY = 2000;
    boolean isBackPressed = false;

    Handler backHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MESSAGE_BACKKEY_TIMEOUT:
                    isBackPressed = false;
                    break;
            }
        }
    };


    //일정시간마다 반복작업

    Handler timeHandler = new Handler();
    private static final long NOT_STARTED = -1L;
    long startTime = NOT_STARTED;

    private final int TIME_INTERVAL = 1000;
    int mCount=20;

    Runnable countDownRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = SystemClock.uptimeMillis();

            if(startTime == NOT_STARTED){
                startTime = currentTime;
            }

            int interval = (int)(currentTime-startTime);
            int delayCount = interval/TIME_INTERVAL;
            int rest = TIME_INTERVAL-(interval%TIME_INTERVAL);

            if(mCount-delayCount>0){
                messageView.setText("count down: "+(mCount-delayCount));
                timeHandler.postDelayed(this,rest);

            }
            else{
                messageView.setText("countdown completed!");
            }
        }
    };

    @Override
    public void onBackPressed() {
        if(!isBackPressed){
            isBackPressed=true;
            Toast.makeText(this, "One More Backkey Press", Toast.LENGTH_SHORT).show();
            backHandler.sendEmptyMessageDelayed(MESSAGE_BACKKEY_TIMEOUT,TIMEOUT_BACKKEY_DELAY);

        }
        else{
            backHandler.removeMessages(MESSAGE_BACKKEY_TIMEOUT);
            super.onBackPressed();
        }
    }

    //    private static final int MESSAGE_PROGRESS_UPDATE = 1;
//    private static final int MESSAGE_PROGRESS_END = 2;
//
    Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case MESSAGE_PROGRESS:
                    int progress = msg.arg1;
                    progressBar.setProgress(progress);
                    messageView.setText("progress: "+ progress);
                    break;

                case MESSAGE_DONE:
                    progressBar.setProgress(100);
                    messageView.setText("progress done");

                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.btnStart);
        Button btn2 = (Button) findViewById(R.id.btnStart2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        messageView = (TextView) findViewById(R.id.message);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(132);

                new MyTask().execute();

//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int count=0;
//                        while(count<20){
//
//                            //handleMessage() 방식
////                            Message msg = mHandler.obtainMessage(MESSAGE_PROGRESS,count*5,0);
////                            mHandler.sendMessage(msg);
//
//                            //post 방식
//                            mHandler.post(new ProgressRunnable(count*5));
//                           count++;
//
//                            try{
//                                Thread.sleep(1000);
//
//                            }catch (InterruptedException e){
//
//                            }
//                        }
//
//                        //handleMessage() 방식
////                        Message msg = mHandler.obtainMessage(MESSAGE_DONE);
////                        mHandler.sendMessage(msg);
//
//                        //post() 방식
//                        mHandler.post(new DoneRunnable());
//                    }
//                }).start();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeHandler.removeCallbacks(countDownRunnable);
                mCount = 20;
                startTime=NOT_STARTED;
                timeHandler.post(countDownRunnable);
            }
        });

        mDialog = new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_launcher_foreground)
                .setTitle("TimeoutDialog")
                .setMessage("This dialog is time out test dialog")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mDialogHandler.removeMessages(MESSAGE_DIALOG_TIMEOUT);
                        Toast.makeText(MainActivity.this, "Timeout canceled!", Toast.LENGTH_SHORT).show();

                    }
                }).create();
        mDialog.show();
        mDialogHandler.sendEmptyMessageDelayed(MESSAGE_DIALOG_TIMEOUT,TIMEOUT_DIALOG_DELAY);
    }


    //-------------------------------------------------------
    //post방식

    class ProgressRunnable implements Runnable {

        int progress;

        public ProgressRunnable(int progress) {
            this.progress = progress;
        }

        @Override
        public void run() {
            progressBar.setProgress(progress);
            messageView.setText("progress: "+ progress);
        }
    }

    class DoneRunnable implements Runnable {
        @Override
        public void run() {
            progressBar.setProgress(100);
            messageView.setText("progress done");
        }
    }

    //-------------------------------------------------------
    //AsyncTask
    class MyTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setMax(100);
            messageView.setText("initialize...");
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            int count=0;
            while(count<20){
                publishProgress(count,count*5);
                count++;
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int count = values[0];
            int progress = values[1];
            progressBar.setProgress(progress);
            messageView.setText("progress : "+progress);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if(aBoolean != null && aBoolean){
                progressBar.setProgress(100);
                messageView.setText("progress done");
            }
        }
    }


    //-------------------------------------------------------


}