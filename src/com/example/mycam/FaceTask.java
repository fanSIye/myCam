package com.example.mycam;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

//import com.arcsoft.facedetection.AFD_FSDKEngine;
//import com.arcsoft.facedetection.AFD_FSDKError;
//import com.arcsoft.facedetection.AFD_FSDKFace;
/**
 * Created by zhousong on 2016/9/28.
 * 单独的任务类。继承AsyncTask，来处理从相机实时获取的耗时操作
 */
public class FaceTask extends AsyncTask{
    private byte[] mData;
    Camera mCamera;
    private static final String TAG = "CameraTag";
    
//    AFD_FSDKEngine AFDengine;
//    List<AFD_FSDKFace> AFDresult;
    //构造函数
    FaceTask(byte[] data , Camera camera)
    {
        this.mData = data;
        this.mCamera = camera;
//        AFDengine = new AFD_FSDKEngine();
//        AFDresult = new ArrayList<AFD_FSDKFace>();
//		AFD_FSDKError err = AFDengine.AFD_FSDK_InitialFaceEngine("CtNPGW212aA3eyrd7ZTUsCRHBzL25CT8GVC5xSdu1CUe",
//				"FyR2coEcDQS4kPEE865TRKw2uxuJLgbwnz1n6AQADVH8", AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
//		Log.d("com.arcsoft", "AFD_FSDK_InitialFaceEngine = " + err.getCode());
    }
    
    @Override
    protected void finalize(){
    	try{
//    		AFD_FSDKError err = AFDengine.AFD_FSDK_UninitialFaceEngine();
//    		Log.d("com.arcsoft", "AFD_FSDK_UninitialFaceEngine =" + err.getCode());
        	super.finalize();
    	} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
    	
    	
    }
    @Override
    protected Object doInBackground(Object[] params) {
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;

        //mData:yuv data,4:2:2
        //Log.i("XXXXXXXXXXXXXX", "w:"+w +",h:"+h + "datalen:"+mData.length);
        //AFD_FSDKError err = AFDengine.AFD_FSDK_StillImageFaceDetection(mData, w, h, AFD_FSDKEngine.CP_PAF_NV21, AFDresult);
		//Log.d("com.arcsoft", "AFD_FSDK_StillImageFaceDetection =" + err.getCode());
//		Log.d("com.arcsoft", "Face=" + AFDresult.size());
//		for (AFD_FSDKFace face : AFDresult) {
//			Log.d("com.arcsoft", "Face:" + face.toString());
//		}
     
//        Rect rect = new Rect(0, 0, w, h);
//        YuvImage yuvImg = new YuvImage(mData, imageFormat, w, h, null);
//        try {
//            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
//            yuvImg.compressToJpeg(rect, 100, outputstream);
//            Bitmap rawbitmap = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
//            Log.i(TAG, "onPreviewFrame: rawbitmap:" + rawbitmap.toString());
//
//            //若要存储可以用下列代码，格式为jpg
//            /* BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
//            img.compressToJpeg(rect, 100, bos);
//            bos.flush();
//            bos.close();
//            mCamera.startPreview();
//            */
//        }
//        catch (Exception e)
//        {
//            Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
//        }
        return null;
    }
}
