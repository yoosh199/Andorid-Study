package com.example.myfile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private String fname = "memo.txt";
    private EditText et;
    private String TAG = "log";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = (EditText) findViewById(R.id.editText);
        File file = new File(getFilesDir(),"memo.txt");//파일 주소값 생성 /data/user/0/com.example.myfile/files/memo.txt
        Log.d(TAG, "file path: "+file);
        if(!file.exists()){//처음엔 이 파일이 없음
            return;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(
                    new InputStreamReader(openFileInput("memo.txt")));
            //InputStreamReader(InputStream in)
            //openFileInput : stirng to InputStream
            et.setText(br.readLine());
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    protected void onStop() {
        super.onStop();
        try {
            BufferedReader br = new BufferedReader(new FileReader("output.txt"));
            String line;
            while((line=br.readLine())!=null){
                //한줄씩 읽는다
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //텍스트 입출력을 위해서는 I/O stream을 reader/writer 로 변환해야함
        try {
            BufferedWriter bw = new BufferedWriter(
                    new OutputStreamWriter(openFileOutput("memo.txt",MODE_PRIVATE))
                    //default 저장경로는 /data/user/0/com.example.myfile/files 밑에인거 같음
                    //openFileOutput : string을 바이너리 스트림이된다.
            );
            bw.write(et.getText().toString());
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}