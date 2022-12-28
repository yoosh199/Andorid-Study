package com.example.fileprovider;

import static androidx.core.content.FileProvider.getUriForFile;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "storage : " +this.getExternalFilesDir(null));
        Log.d(TAG, "storage : " +this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        Log.d(TAG, "storage : " +Environment.getExternalStorageDirectory());
        Log.d(TAG, "storage : " +this.getFilesDir());
        Log.d(TAG, "storage : " +Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES));



        runtimePermission();

    }

    private void runtimePermission() {
        Dexter.withContext(this).withPermissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(new MultiplePermissionsListener() {
            @Override
            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                //허용되면
                //IMG_20221220_080156.jpg : 카메라로 사진 찍어서 파일명 넣기
                File newFile = new File(System.getenv("EXTERNAL_STORAGE")+"/Pictures/IMG_20221220_080156.jpg");
                //System.getenv("EXTERNAL_STORAGE")+"/Pictures/IMG_20221220_080156.jpg"
                //"/storage/emulated/0/Pictures/IMG_20221220_080156.jpg"
                //System.getenv("EXTERNAL_STORAGE") = sdcard
                //sdcard = storage/emulated/0 인거 같음
                Uri contentUri = getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", newFile);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(contentUri, "image/jpeg");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            }

            @Override
            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                permissionToken.continuePermissionRequest();
            }
        }).check();
    }
}