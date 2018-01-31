package com.example.frsdktest;

import com.example.mycam.MainActivity;

import android.util.Log;
import android.app.Activity;
import android.os.Environment;
import android.os.Message;


public class CaffeMobile {
	public MainActivity activity;
	public void sendMsgToMain(String info){
        Message msg = new Message();  
        msg.what = 10;
        msg.obj = info;
        activity.msgHandler.sendMessage(msg);  		
	}
	
	static {
	    System.loadLibrary("FaceDetect");
	    System.loadLibrary("FaceFeature");
	
	}
	
	private String filePath = Environment.getExternalStorageDirectory()+"/FRSDK";
	public long [] DetectHandle = new long [1];
	public long [] FeatureHandle = new long [1];

	
	public CaffeMobile(Activity activity){
		this.activity = (MainActivity) activity;
		Log.d(CaffeMobile.class.getSimpleName(), "begin to init");
//		int flag = FDInit(filePath + "/ldmk.param.bin", filePath + "/ldmk.bin", DetectHandle);
		Log.wtf("fr=========", "filepath = :" + filePath);
		sendMsgToMain("base:" + filePath);

		int flag = FDInit(activity, filePath + "/ldmk.param.bin", filePath + "/ldmk.bin", DetectHandle);
		System.out.println("FDInit flag = "+ flag);  
		Log.wtf("fr=========", "FDInit flag = "+ flag);
		sendMsgToMain("FDInit flag = "+ flag + "(非0则失败)");
		if(flag != 0)
		{
//			Message message = null;
//			String sss = "FDInit flag = " + flag;
///			message = Message.obtain(activity.getHandler(),R.id.finish_test_ComputeSimCos,sss);
///			message.sendToTarget(); 
		 }
		  flag = FFInit(activity, filePath + "/fea.param.bin", filePath + "/fea.bin", FeatureHandle);
		  Log.wtf("fr=========", "FFInit flag = "+ flag);
		  Log.wtf("fr=========", "DetectHandle = "+ DetectHandle[0]);
		  Log.wtf("fr=========", "FeatureHandle = "+ FeatureHandle[0]);
		  Log.wtf("fr=========", "FDVersion = "+ FDgetVersion());
		  Log.wtf("fr=========", "FFVersion = "+ FFgetVersion());
			sendMsgToMain("FFInit flag = "+ flag + "(非0则失败)");
			sendMsgToMain("DetectHandle = "+ DetectHandle[0]);
			sendMsgToMain("FeatureHandle = "+ FeatureHandle[0]);
			sendMsgToMain("FDVersion = "+ FDgetVersion());
			sendMsgToMain("FFVersion = "+ FFgetVersion());

		 System.out.println("FFInit flag = "+ flag);  
		 System.out.println("DetectHandle = "+ DetectHandle[0]);  
		 System.out.println("FeatureHandle = "+ FeatureHandle[0]);  
		 System.out.println("FDVersion = "+ FDgetVersion());  
		 System.out.println("FFVersion = "+ FFgetVersion());  
		 
//		 if(flag != 0)
//		 {
//			 Message message = null;
//			 String sss = "FFInit flag = " + flag;
///			message = Message.obtain(activity.getHandler(),R.id.finish_test_ComputeSimCos,sss);
///			message.sendToTarget(); 
//		 }
	}
	public void Destroy(){
		 FDDestroy(DetectHandle[0]);
		 FFDestroy(FeatureHandle[0]);
		
	}
	public native void enableLog(boolean enabled);
//	public native int FDInit(String ldmkparam, String ldmkbin, long [] handle);
	public native int FDInit(Activity activity, String ldmkparam, String ldmkbin, long [] handle);
	public native void FDDestroy(long handle); 
	public native int FDDetect(long handle, byte [] BGR, int width, int height, double [] rect);
	public native int FDDetectpath(long handle, String imgpath, double [] rect);
	public native String FDgetVersion();
	public native int FFInit(Activity activity, String feaparam, String feabin, long [] handle);
	public native void FFDestroy(long handle); 
	public native int FFFeaExtract(long handle, byte [] BGR, int width, int height, double [] fea, double [] rect);
	public native int FFFeaExtractPath(long handle, String imgpath, double [] fea, double [] rect);
	public native double FFSimilarity(double [] feaA, double [] feaB);
	public native String FFgetVersion();

    
}
