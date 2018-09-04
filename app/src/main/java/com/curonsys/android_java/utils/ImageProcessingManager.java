package com.curonsys.android_java.utils;

import android.graphics.Bitmap;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

/**
 * Created by ijin-yeong on 2018. 7. 26..
 */

public class ImageProcessingManager {
    private FeatureDetector detector = FeatureDetector.create(FeatureDetector.SIFT);

    static {
        try {
            System.loadLibrary("opencv_java");
        } catch (UnsatisfiedLinkError e) {
            System.load("opencv_java");
        }
        try {
            System.loadLibrary("nonfree");
        } catch (UnsatisfiedLinkError e) {
            System.load("nonfree");
        }
    }

    public ImageProcessingManager() {
        int a;
        a = 1;
    }

    public double sift(Bitmap inputImage){
        Mat rgba=new Mat();
        Utils.bitmapToMat(inputImage,rgba);
        // scaling 기능 추가 할것

        MatOfKeyPoint keyPoints=new MatOfKeyPoint();
        Imgproc.cvtColor(rgba,rgba,Imgproc.COLOR_RGBA2GRAY);
        detector.detect(rgba,keyPoints);
        Features2d.drawKeypoints(rgba,keyPoints,rgba);
        Utils.matToBitmap(rgba,inputImage);
//        iv_view.setImageBitmap(inputImage);
        //sift_tv_result.setText("Keypoint 갯수 : " + keyPoints.toArray().length);
        double image_size = rgba.size().width*rgba.size().height;
        return keyPoints.toArray().length/image_size;
    }
}
