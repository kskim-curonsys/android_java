package com.curonsys.android_java;

/**
 * Created by ijin-yeong on 2018. 5. 16..
 */
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.curonsys.android_java.ChooseActivity;

import java.util.ArrayList;

/**
 * Created by Jinyoung on 2018-02-02.
 */

public class PermissionManager {
    private Context context;
    private ArrayList<String> permission_check_list = new ArrayList<>();

    PermissionManager(Context context){
        this.context = context;

        //PUT YOUR PERMISSION LIST!!
        this.permission_check_list.add(Manifest.permission.CAMERA);
        this.permission_check_list.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        this.permission_check_list.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        this.permission_check_list.add(Manifest.permission.READ_PHONE_STATE);
        this.permission_check_list.add(Manifest.permission.ACCESS_FINE_LOCATION);
        this.permission_check_list.add(Manifest.permission.ACCESS_NETWORK_STATE);
    }

    public void permissionCheck(){
        ArrayList<String> deninedPermission = new ArrayList<>();

        for(int i=0;i<permission_check_list.size();i++){
            if(ContextCompat.checkSelfPermission(context, permission_check_list.get(i)) == PackageManager.PERMISSION_DENIED){
                Log.d("debug",permission_check_list.get(i));
                deninedPermission.add(permission_check_list.get(i));
                //권한 request 할 permissions list 추리기
            }
        }

        // Should we show an explanation?
        if (!deninedPermission.isEmpty()) {
            //cascading for string...
            String[] permissions = new String [deninedPermission.size()];
            deninedPermission.toArray(permissions);
            //request permission
            ActivityCompat.requestPermissions((ChooseActivity)context,
                    permissions, 0);
        } else {
            // No explanation needed, we can request the permission.
            //Toast.makeText(context,"요청할 권한이 없네요 ^^",Toast.LENGTH_SHORT).show();
        }
    }
}