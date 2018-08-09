package com.curonsys.android_java;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import eu.kudan.kudan.ARActivity;

public class ARCameraActivity extends ARActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
    }
}
