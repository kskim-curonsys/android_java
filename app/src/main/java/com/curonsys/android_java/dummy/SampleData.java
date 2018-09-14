package com.curonsys.android_java.dummy;


import com.curonsys.android_java.R;

import java.util.ArrayList;

public class SampleData {

    //contentName
    public static String[] nameArray = {"snake", "car", "helicopter", "bigben"};

    //contentDescribe
    public static String[] versionArray = {"뱀", "자동차", "헬리콥터", "빅밴"};

    //contentPreview
    public static Integer[] drawableArray = {R.drawable.cupcake, R.drawable.donut, R.drawable.eclair,
            R.drawable.froyo};

    //contentId
    public static String[] id_ = {"0", "1", "2", "3"};
    //contentFileName
    public static String[] contentFileName = {"snake.jet","car.jet","helicopter.jet","bigben.jet"};
    public static ArrayList<String[]> contentTextureFiles = new ArrayList<>();
    public static ArrayList<String[]> contentTextureNames = new ArrayList<>();
    public static boolean[] contentHasAnimation = {true,false,false,false};
    public static int[] contentTextureCount = {3,1,1,1};

    public SampleData(){
        contentTextureFiles.add(new String[]{"lengua.jpg","ojo.jpg","cuerpo.jpg"});
        contentTextureFiles.add(new String[]{"car.jpg"});
        contentTextureFiles.add(new String[]{"helicopter.jpg"});
        contentTextureFiles.add(new String[]{"bigben.png"});

        contentTextureNames.add(new String[]{"lengua","ojo","cuerpo"});
        contentTextureNames.add(new String[]{"car"});
        contentTextureNames.add(new String[]{"helicopter"});
        contentTextureNames.add(new String[]{"bigben"});
    }

    private static SampleData instance;

    public static SampleData getInstance () {
        if (instance == null)
            instance = new SampleData();
        return instance;
    }
}
