package com.example.mycam;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

//import com.arcsoft.facedetection.AFD_FSDKEngine;
//import com.arcsoft.facedetection.AFD_FSDKError;
//import com.arcsoft.facedetection.AFD_FSDKFace;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
//import com.arcsoft.facetracking.*;
import redis.clients.jedis.Jedis;

import com.example.frsdktest.CaffeMobile;
import com.example.mycam.*;


/**
 * Created by zhousong on 2016/9/19.
 * 相机界面SurfaceView的回调类
 */
public final class SurfaceViewCallback implements android.view.SurfaceHolder.Callback, Camera.PreviewCallback {

	private static int FACEFEATURESIZE = 512;
	private static int MAXFACECOUNT = 50;
	private static int PARAMSIZE = 20;
    double [] feaA = new double[FACEFEATURESIZE];
    double [] feaB = new double[FACEFEATURESIZE];
    double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
    double [] rectarrayB = new double[PARAMSIZE * MAXFACECOUNT]; //left top width height nQuality
    int facecountA = 0;
    int facecountB = 0;
    Bitmap bmp = null;
    byte [] BGRA;
    int _running = 0;
    
    Context context;
    Activity acvitity;
    InformationView informationView;
	//private CaffeMobile caffeMobile;
    static final String TAG = "CameraCCCC";
    FrontCamera mFrontCamera = new FrontCamera();
    boolean previewing = mFrontCamera.getPreviewing();
    Camera mCamera;
    FaceTask mFaceTask;
//    Jedis jedis = new Jedis("192.168.1.60",6379);
    Jedis jedis = new Jedis(GloableConfig.redisServerIP,GloableConfig.redisServerPort);


	//AFT_FSDKEngine AFTengine;
	//List<AFT_FSDKFace> AFTresult;
    
    //AFD_FSDKEngine AFDengine;
    //List<AFD_FSDKFace> AFDresult;
    
    ByteArrayOutputStream jpegStream;
    byte[] redisKeyName = {'I','n','S','e','e','f','r','a','m','e','L','i','s','t'};
    byte[] redisKeyBuffer;
    byte[] faceJpegBytes;
    byte[] redisFullBuffer = new byte[5000000];//5M


    public void setContext(Context context) {
        this.context = context;
    }
    public void setActivity(MainActivity acvitity) {
        this.acvitity = acvitity;
    }

    public void setInformationView(InformationView view) {
        this.informationView = view;
    }

    public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
        if (previewing) {
            mCamera.stopPreview();
            Log.i(TAG, "停止预览");
        }

        try {
            mCamera.setPreviewDisplay(arg0);
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
            Log.i(TAG, "开始预览");
            //调用旋转屏幕时自适应
            //setCameraDisplayOrientation(MainActivity.this, mCurrentCamIndex, mCamera);
        } catch (Exception e) {
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
    	jpegStream = new ByteArrayOutputStream();
        //初始化前置摄像头
        mFrontCamera.setCamera(mCamera);
        mCamera = mFrontCamera.initCamera();
        mCamera.setPreviewCallback(this);
        //适配竖排固定角度
        Log.i(TAG, "context: " + context.toString());
        Log.i(TAG, "mFrontCamera: " + mFrontCamera.toString());
        Log.i(TAG, "mCamera: " + mCamera.toString());
        
        
		//caffeMobile = new CaffeMobile(acvitity);
//		caffeMobile = new CaffeMobile(activity);
        //AFDengine = new AFD_FSDKEngine();
        //AFTengine = new AFT_FSDKEngine();
        //AFDresult = new ArrayList<AFD_FSDKFace>();
        //AFTresult = new ArrayList<AFT_FSDKFace>();

		//AFD_FSDKError AFDerr = AFDengine.AFD_FSDK_InitialFaceEngine("CtNPGW212aA3eyrd7ZTUsCRHBzL25CT8GVC5xSdu1CUe",
		//		"FyR2coEcDQS4kPEE865TRKw2uxuJLgbwnz1n6AQADVH8", AFD_FSDKEngine.AFD_OPF_0_HIGHER_EXT, 16, 5);
//		Log.d("com.arcsoft", "AFD_FSDK_InitialFaceEngine = " + AFDerr.getCode());

		
		//AFT_FSDKError AFTerr = AFTengine.AFT_FSDK_InitialFaceEngine("CtNPGW212aA3eyrd7ZTUsCRHBzL25CT8GVC5xSdu1CUe",
		//		"FyR2coEcDQS4kPEE865TRKvukZeBPpSTRHtQABUdxFeF", AFTengine.AFT_OPF_0_HIGHER_EXT, 16, 5);
		//Log.d("com.arcsoft", "AFD_FSDK_InitialFaceEngine = " + AFTerr.getCode());
		
		//new Thread(jedisConnect).start();
        //FrontCamera.setCameraDisplayOrientation((Activity) context, mFrontCamera.getCurrentCamIndex(), mCamera);
        
        GloableConfig gloableConfig = new GloableConfig();
    	redisKeyBuffer = gloableConfig.getRedisKey().getBytes();
    	System.arraycopy(redisKeyBuffer, 0, redisFullBuffer, 0, 32);
//    	System.arraycopy(  (byte_1, 0, byte_3, 0, byte_1.length);  

    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    	try{
    	//	AFD_FSDKError AFDerr = AFDengine.AFD_FSDK_UninitialFaceEngine();
    	//	Log.d("com.arcsoft", "AFD_FSDK_UninitialFaceEngine =" + AFDerr.getCode());
    	//	AFT_FSDKError AFTerr = AFTengine.AFT_FSDK_UninitialFaceEngine();
    	//	Log.d("com.arcsoft", "AFD_FSDK_UninitialFaceEngine =" + AFTerr.getCode());
    	//	new Thread(jedisDisconnect).start();
        	super.finalize();
    	} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
        mFrontCamera.StopCamera(mCamera);
    }

    /**
     * 相机实时数据的回调
     *
     * @param data   相机获取的数据，格式是YUV
     * @param camera 相应相机的对象
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

	    if (mFaceTask != null) {
            switch (mFaceTask.getStatus()) {
                case RUNNING:
                    return;
                case PENDING:
                    mFaceTask.cancel(false);
                    break;
            }

        }
        
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;
        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(data, imageFormat, w, h, null);
        try {
        	ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        	yuvImg.compressToJpeg(rect, 100, outputstream);
        	bmp = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
        	
//		    Runnable FRProcess = new Runnable() {    	  
//		        @Override  
//		        public void run() {
//		        	if(_running == 0){
//		        		_running = 1;
//		    		    BGRA = getPixelsBGR(bmp);
//			        	facecountA = caffeMobile.FDDetect(caffeMobile.DetectHandle[0], BGRA, bmp.getWidth(), bmp.getHeight(), rectarrayA);
//						informationView.IAFResult.clear();
//						if(facecountA > 0 )
//						{
//							System.out.println("facecountA = :" + facecountA);
//							//System.out.println("rectarrayA = :" + rectarrayA.length);
//							informationView.IAFResult.add(rectarrayA);
//							int feaex = caffeMobile.FFFeaExtract(caffeMobile.FeatureHandle[0], BGRA, bmp.getWidth(), bmp.getHeight(), feaA, rectarrayA);
//							System.out.println("feaex = :" + feaex);
//						}
//						else
//						{
//							Log.d(TAG, "A: detect no face");
//						}
//		        		_running = 0;		        		
//		        	}
//		        }  
//		    };
//
//		    new Thread(FRProcess).start();
		    //facecountA = caffeMobile.FDDetect(caffeMobile.DetectHandle[0], BGRA, bmp.getWidth(), bmp.getHeight(), rectarrayA);
		 
//        	informationView.bmpToShow = bmp;
        	informationView.bmpToFDR = bmp;
//			drawFlag();
        	
//        	Log.i(TAG, "onPreviewFrame: rawbitmap:" + informationView.bmpToShow.toString());

		  //若要存储可以用下列代码，格式为jpg
		  /* BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
		  img.compressToJpeg(rect, 100, bos);
		  bos.flush();
		  bos.close();
		  mCamera.startPreview();
		  */
		}
		catch (Exception e)
		{
		    Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
		}

        
        //Camera.Parameters parameters = camera.getParameters();

        Size size = camera.getParameters().getPreviewSize();
        //AFDresult.clear();
        //AFTresult.clear();
        
        //AFD_FSDKError AFDerr = AFDengine.AFD_FSDK_StillImageFaceDetection(data, 
        //		size.width, size.height, AFD_FSDKEngine.CP_PAF_NV21, AFDresult);
        
//        AFT_FSDKError AFTerr = AFTengine.AFT_FSDK_FaceFeatureDetect(data, 
//        		size.width, size.height, AFT_FSDKEngine.CP_PAF_NV21, AFTresult);
        
//		Log.d("com.arcsoft", "AFD_FSDK_StillImageFaceDetection =" + AFDerr.getCode());
//		Log.d("com.arcsoft", "Face=" + AFDresult.size());
//		for (AFD_FSDKFace face : AFDresult) {
//			Log.d("com.arcsoft", "Face:" + face.toString());
//			
//		}
        
    	//if(AFDresult.size() > 0){
    	//	yv12FaceToBmp(data);
    	//	drawFlag();
    	//}

        //mFaceTask = new FaceTask(data, camera);
        //mFaceTask.execute((Void) null);
        //Log.i(TAG, "onPreviewFrame: 启动了Task");

    }
    
    public void yv12FaceToBmp(byte[] data) {
    	
    	Bitmap tmpBmp;
        Matrix matrix = new Matrix();
        matrix.setRotate(-90);
        
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;
        informationView.orgBmpHeight = h;
        informationView.orgBmpWidth = w;
        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(data, imageFormat, w, h, null);
        try {
        	ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        	yuvImg.compressToJpeg(rect, 100, outputstream);
        	tmpBmp = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
//        	for (AFD_FSDKFace face : AFDresult){
//            	informationView.bmpToShow = Bitmap.createBitmap(tmpBmp,
//            			face.getRect().left,
//            			face.getRect().top,
//            			face.getRect().width(),
//            			face.getRect().height(),
//            			matrix, false);
//            	tmpBmp.recycle();
//            	jpegStream.reset();
//            	
//                informationView.bmpToShow.compress(CompressFormat.JPEG, 50, jpegStream);
//                faceJpegBytes = jpegStream.toByteArray();
//        		new Thread(jedisWrite).start();
//
//        		break;
//        	}
        	//Log.i(TAG, "onPreviewFrame: rawbitmap:" + informationView.bmpToShow.toString());

		  //若要存储可以用下列代码，格式为jpg
		  /* BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
		  img.compressToJpeg(rect, 100, bos);
		  bos.flush();
		  bos.close();
		  mCamera.startPreview();
		  */
		}
		catch (Exception e)
		{
		    Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
		}

    }

    public void yv12ToBmp(byte[] data) {
        Camera.Parameters parameters = mCamera.getParameters();
        int imageFormat = parameters.getPreviewFormat();
        int w = parameters.getPreviewSize().width;
        int h = parameters.getPreviewSize().height;
        Rect rect = new Rect(0, 0, w, h);
        YuvImage yuvImg = new YuvImage(data, imageFormat, w, h, null);
        try {
        	ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
        	yuvImg.compressToJpeg(rect, 100, outputstream);
        	informationView.bmpToFDR = BitmapFactory.decodeByteArray(outputstream.toByteArray(), 0, outputstream.size());
//        	Log.i(TAG, "onPreviewFrame: rawbitmap:" + informationView.bmpToShow.toString());

		  //若要存储可以用下列代码，格式为jpg
		  /* BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(Environment.getExternalStorageDirectory().getPath()+"/fp.jpg"));
		  img.compressToJpeg(rect, 100, bos);
		  bos.flush();
		  bos.close();
		  mCamera.startPreview();
		  */
		}
		catch (Exception e)
		{
		    Log.e(TAG, "onPreviewFrame: 获取相机实时数据失败" + e.getLocalizedMessage());
		}

    }

    
    
    public void drawFlag(){

//    	informationView.AFDresult = this.AFDresult;
//    	informationView.AFTresult = this.AFTresult;        
    	informationView.invalidate();
    }
    
    
//====================================
    Runnable jedisConnect = new Runnable() {    	  
        @Override  
        public void run() {  
        	///jedis.connect();
        	///jedis.auth(GloableConfig.redisServerPwd);
        }  
    };
    
    Runnable jedisDisconnect = new Runnable() {    	  
        @Override  
        public void run() {  
        	///jedis.disconnect();
        }  
    };
    
//	Runnable jedisRead = new Runnable() {    	  
//        @Override  
//        public void run() {  
//            Log.v("Redis", "READ:::::::::::::::::");
//            try{
//                redisText = jedis.rpop("jediskey");// get("nachos");
//                if(redisText != null){
//                	Log.v("Redis", redisText);
//                }
//            }
//            catch(Exception e){
//            	Log.v("Redis", e.toString());
//            }
//        }  
//    };    

    Runnable jedisWrite = new Runnable() {
    	//redisKeyBuffer = GloableConfig.getRedisKey();
        @Override  
        public void run() {
        	try{
                Log.v("Redis", "WRITE:::::::::::::::::");
                //InSeeframeList
            	System.arraycopy(faceJpegBytes, 0, redisFullBuffer, 32, faceJpegBytes.length);
                ///jedis.lpush(redisKeyName, redisFullBuffer, faceJpegBytes.length + 32);
//                jedis.lpush(redisKeyName, redisFullBuffer);
        	}
        	catch(Exception e){
        		Log.v("Redis", e.toString());
        	}
        }  
    };    
    
    @SuppressLint("NewApi")
	public byte[] getPixelsBGR(Bitmap image) {  
        // calculate how many bytes our image consists of  
        int bytes = image.getByteCount();  
  
        ByteBuffer buffer = ByteBuffer.allocate(bytes); // Create a new buffer  
        image.copyPixelsToBuffer(buffer); // Move the byte data to the buffer  
  
        byte[] temp = buffer.array(); // Get the underlying array containing the data.  
  
        byte[] pixels = new byte[(temp.length/4) * 3]; // Allocate for BGR  
  
        // Copy pixels into place  
        for (int i = 0; i < temp.length/4; i++) {  
            
            pixels[i * 3] = temp[i * 4 + 2];        //B  
            pixels[i * 3 + 1] = temp[i * 4 + 1];    //G      
            pixels[i * 3 + 2] = temp[i * 4 ];       //R  
                         
        }  
  
        return pixels;  
    }

}