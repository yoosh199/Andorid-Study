package com.example.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    String TAG = "MainActivity";


    Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn= (Button) findViewById(R.id.change_btn);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        FragmentExample1 fragmentExample1 = new FragmentExample1();
        FragmentExample3 fragmentExample3 = new FragmentExample3();
        fragmentTransaction.add(R.id.container1, fragmentExample3);

        fragmentTransaction.add(R.id.container1, fragmentExample1).addToBackStack("fragment1");
        //addToBackStack 으로 백스텍에 추가해서 뒤로가기 누르면 바로종료안되거 백스택에서 fragment1 팝업
        //즉 앱 실행시킨다음 누르면 fragment1 없어짐
        fragmentTransaction.commit();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "onClick");

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                FragmentExample2 fragmentExample2 = new FragmentExample2();
                fragmentTransaction.replace(R.id.container1, fragmentExample2);
                //add, replace 교체해가면 실행해보기
                fragmentTransaction.commit();
            }
        });

    }
    
    @Override
    public void onBackPressed() {
        //이전 프래그먼트를 불러올수 있는 방법은 없을까?
        getSupportFragmentManager().popBackStack();

        super.onBackPressed();
    }
}
