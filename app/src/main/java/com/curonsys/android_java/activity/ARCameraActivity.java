package com.curonsys.android_java.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import eu.kudan.kudan.ARActivity;
import com.curonsys.android_java.R;

public class ARCameraActivity extends ARActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arcamera);
    }
}
