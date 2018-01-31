package com.example.mycam;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.text.format.DateFormat;
import android.util.Log;
import com.example.frsdktest.*;

public class FRSDKThread implements Runnable{

	private InformationView informationView = null;
	private static final String TAG = FRSDKThread.class.getSimpleName();
	private CaffeMobile caffeMobile;
	private MainActivity activity;
	private static int FACEFEATURESIZE = 512;
	private static int MAXFACECOUNT = 50;
	private static int PARAMSIZE = 20;
	List<TargetInfo> targetInfos = new ArrayList<TargetInfo>();
	private SimpleDateFormat df;
	public int _stop = 0;
	public int _targetPicsChanged = 1;
	
	double threshold = 0.62;
	
	public void setInformationView(InformationView iv){
		informationView = iv;
	}
	
	public FRSDKThread(MainActivity activity){
		this.activity = activity;
	}
	@SuppressLint("NewApi") public byte[] getPixelsBGR(Bitmap image) {  
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
	 @SuppressLint("SimpleDateFormat") public void testBGR(CaffeMobile caffeMobile) throws FileNotFoundException
	 {
		 
		 	Date d1, d2;
		 	long diff;
		 
		    Message message = null;
		    double [] feaA = new double[FACEFEATURESIZE];
		    double [] feaB = new double[FACEFEATURESIZE];
		    double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
		    double [] rectarrayB = new double[PARAMSIZE * MAXFACECOUNT]; //left top width height nQuality
		    int facecountA = 0;
		    int facecountB = 0;
		  
		    //Aͼ�������������ȡ����
		    String filePathA = Environment.getExternalStorageDirectory()+"/FRSDK/008.jpg";
		    FileInputStream fis = new FileInputStream(filePathA);
		    Bitmap bitmapA = BitmapFactory.decodeStream(fis);
		    byte [] BGRA = getPixelsBGR(bitmapA);

		    facecountA = caffeMobile.FDDetect(caffeMobile.DetectHandle[0], BGRA, bitmapA.getWidth(), bitmapA.getHeight(), rectarrayA);
		 
			if(facecountA > 0 )
			{
				
				caffeMobile.FFFeaExtract(caffeMobile.FeatureHandle[0], BGRA, bitmapA.getWidth(), bitmapA.getHeight(), feaA, rectarrayA);
				
				
			}
			else
			{
				Log.d(TAG, "A: detect no face");
				return;
			}
				
			//Bͼ�������������ȡ����
			 String filePathB = Environment.getExternalStorageDirectory()+"/FRSDK/009.jpg";
			 FileInputStream fisB = new FileInputStream(filePathB);
			 Bitmap bitmapB = BitmapFactory.decodeStream(fisB);
			 byte [] BGRB = getPixelsBGR(bitmapB);
			    
			
			
			facecountB = caffeMobile.FDDetect(caffeMobile.DetectHandle[0], BGRB, bitmapB.getWidth(), bitmapB.getHeight(), rectarrayB);
		
			if(facecountB > 0 )
			{
				caffeMobile.FFFeaExtract(caffeMobile.FeatureHandle[0], BGRB, bitmapB.getWidth(), bitmapB.getHeight(), feaB, rectarrayB);
			}
			else
			{
				Log.d(TAG, "B: detect no face");
				return;
			}
			
			if(facecountA > 0 && facecountB > 0)
			{
				double  score = caffeMobile.FFSimilarity(feaA, feaB);
				System.out.println("score = :" + score);
				Log.d("fr=========", "score = :" + score);
///				message = Message.obtain(activity.getHandler(),R.id.finish_test_ComputeSimCos,score);
///				message.sendToTarget();
				
			}
	 }
	
	
	 @SuppressLint("SimpleDateFormat") public void test(CaffeMobile caffeMobile)
	 {
		 
		 	Date d1, d2;
		 	long diff;
		 
		    Message message = null;
		    double [] feaA = new double[FACEFEATURESIZE];
		    double [] feaB = new double[FACEFEATURESIZE];
		    double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
		    double [] rectarrayB = new double[PARAMSIZE * MAXFACECOUNT]; //left top width height nQuality
		    int facecountA = 0;
		    int facecountB = 0;
		  
		    //Aͼ�������������ȡ����
		    String filePathA = Environment.getExternalStorageDirectory()+"/FRSDK/008.jpg";
		    
		    facecountA = caffeMobile.FDDetectpath(caffeMobile.DetectHandle[0], filePathA, rectarrayA);
			if(facecountA > 0 )
			{
				caffeMobile.FFFeaExtractPath(caffeMobile.FeatureHandle[0], filePathA, feaA, rectarrayA);
			}
			else
			{
				Log.d(TAG, "A: detect no face");
				return;
			}
				
			//Bͼ�������������ȡ����
			 String filePathB = Environment.getExternalStorageDirectory()+"/FRSDK/009.jpg";

			facecountB = caffeMobile.FDDetectpath(caffeMobile.DetectHandle[0], filePathB, rectarrayB);
			
			if(facecountB > 0 )
			{
				caffeMobile.FFFeaExtractPath(caffeMobile.FeatureHandle[0], filePathB, feaB, rectarrayB);
			}
			else
			{
				Log.d(TAG, "B: detect no face");
				return;
			}
			
			if(facecountA > 0 && facecountB > 0)
			{
				//Date dt= new Date();
				Long t1= System.currentTimeMillis();
				System.out.println("t1="+t1);
				double  score = 0;
				for(int a = 0; a < 10000; a++)
				{
					score = caffeMobile.FFSimilarity(feaA, feaB);
				}
				long t2 = System.currentTimeMillis();
				System.out.println("t2="+t2);
				
				System.out.println("score = :" + score+", 10000 times dt = "+(t2-t1) );
				Log.d("fr=========", "score = :" + score);
				
///				message = Message.obtain(activity.getHandler(),R.id.finish_test_ComputeSimCos,score);
///				message.sendToTarget();
				
			}
	 }

	public void makeModelFiles(CaffeMobile caffeMobile)
	{
		double [] feaA = new double[FACEFEATURESIZE];
		double [] feaB = new double[FACEFEATURESIZE];
		double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
		int cnt = 0;
		String fileAname = "";
		String pathAname = "";
		String modelAname = "";
		int idx = 0;
		String fileAbsolutePath = GloableConfig.targetPicPath;
        File file = new File(fileAbsolutePath);
        MyFilter fnf = new MyFilter(".jpg");

        File[] subFile = file.listFiles(fnf);  
  
        if(subFile == null){
        	System.out.println("====, no target pic fiels found.");
            sendMsgToMain("�޼����Ƭ.");
        	return; 
        }
        sendMsgToMain("��Ƭ������"+subFile.length);

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {  
            // �ж��Ƿ�Ϊ�ļ���  
            if (!subFile[iFileLength].isDirectory()) {  
                String filename = subFile[iFileLength].getName();
                subFile[iFileLength].getAbsolutePath();
                // �ж��Ƿ�ΪMP4��β  
//                if (filename.trim().toLowerCase().endsWith(".jpg")) 
                {
                	if( ((iFileLength+1) % 1000 == 0) ||
                			(iFileLength+1>=subFile.length)){
        		        sendMsgToMain("��������"+(iFileLength+1));                		
                	}
                	fileAname = subFile[iFileLength].getAbsolutePath();
                	pathAname = subFile[iFileLength].getParent();
                	modelAname = fileAname.substring(0, fileAname.length() - 3) + "mmia";
                	idx++;
    				//System.out.println("====, file=["+fileAname+"]-[" + modelAname + "], idx = "+ idx);
    				File filetest = new File(modelAname);
    				//�ж��ļ����Ƿ����,����������򴴽��ļ���
    				if (!filetest.exists()) {
        				System.out.println("====, file=["+modelAname+"] not exist, create it. ");
    					
        				cnt = caffeMobile.FDDetectpath(caffeMobile.DetectHandle[0], fileAname, rectarrayA);
        				System.out.println("====, cnt = " + cnt);
	    				if(cnt > 0 )
	    				{
	        				System.out.println("====, found face, save model to file.");
	        		        sendMsgToMain("����Ƭ��"+subFile[iFileLength].getName() + "����ģ�ͳɹ�.");
	        				
	    					caffeMobile.FFFeaExtractPath(caffeMobile.FeatureHandle[0], fileAname, feaA, rectarrayA);
	    					createFileWithByte(DoubleArrayToByteArray(feaA), modelAname);
	    				}
	    				else
	    				{
	    					Log.d(TAG, "A: detect no face");
	        		        sendMsgToMain("����Ƭ��"+subFile[iFileLength].getName() + "����ģ��ʧ��.");
	    				}
    				}
                }  
            }  
        }
	 }

	private class TargetInfo{
		String id;
		double [] fea;
	}
	
	public void testDiff(CaffeMobile caffeMobile)
	{
		double [] feaA = new double[FACEFEATURESIZE];
		double [] feaB = new double[FACEFEATURESIZE];
		double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
		double threshold = 0.65;
		//List<double[]> listA = new List<double[]>;
		List<TargetInfo> listA = new ArrayList<TargetInfo>();
		List<TargetInfo> listB = new ArrayList<TargetInfo>();
		int facecountA = 0;
		String fileAname = "";
		String pathAname = "";
		String modelAname = "";

		listA.clear();
		listB.clear();
		
        String fileAbsolutePath = Environment.getExternalStorageDirectory() + "/FRSDK/t1";
	      String strContent="";
	      
	      try {
		      String strLogFilePath = fileAbsolutePath+"/log.txt";
	           File logFile = new File(strLogFilePath);
	           if (!logFile.exists()) {
	            Log.d("TestFile", "Create the file:" + strLogFilePath);
	            logFile.createNewFile();
	           }
	           RandomAccessFile raf = new RandomAccessFile(strLogFilePath, "rw");
	           raf.seek(logFile.length());
//	           raf.write(strContent.getBytes());
	           
	           
	        File file = new File(fileAbsolutePath);  
	        File[] subFile = file.listFiles();  
			System.out.println("loading model files");

	        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {  
	            // �ж��Ƿ�Ϊ�ļ���  
	            if (!subFile[iFileLength].isDirectory()) {  
	                String filename = subFile[iFileLength].getName();
	                subFile[iFileLength].getAbsolutePath();
	                // �ж��Ƿ�ΪMP4��β  
	                if (filename.trim().toLowerCase().endsWith(".mmia")) {
	                	fileAname = subFile[iFileLength].getAbsolutePath();
	    				//System.out.println("loading model file=["+fileAname+"]");
	    				
	    				TargetInfo tiA = new TargetInfo();
	    				TargetInfo tiB = new TargetInfo();
	    				
	    				tiA.id = new String(subFile[iFileLength].getName().replace(".mmia", ""));
	    				tiA.fea = byteArryToDoubleArray(readFileToByte(fileAname));
	    				
	    				tiB.id = new String(subFile[iFileLength].getName().replace(".mmia", ""));;
	    				tiB.fea = byteArryToDoubleArray(readFileToByte(fileAname));
    					
	    				listA.add(tiA);
    					listB.add(tiB);
//    					System.out.println("feaA size = " + feaA.length);
//    					System.out.println("feaB size = " + feaB.length);
//	    					for (int mm = 0; mm < feaA.length; mm++)
//	    					{
//		    					System.out.println("a:" + feaA[mm] + ", b:"+feaB[mm]+",d:"+(feaA[mm]-feaB[mm]));
//	    					}
	                }  
	            }  
	        }  
			System.out.println("loading model files finished.");
			System.out.println("listA size = " + listA.size());
			System.out.println("listB size = " + listB.size());
			int sizeA = listA.size();
			int sizeB = listB.size();
			double  score = 0;
			System.out.println("Compare begin.--------------------------");
			strContent = threshold + "--------------------------------\n";
			raf.write(strContent.getBytes());
			for(int i = 0; i<sizeA; i++){
				System.out.println("-------------------------- " + i + " ---");
				for(int j = i+1; j<sizeB; j++){
					//System.out.println("[" + listA.get(i).id +"]-["+listB.get(j).id +"]");
					//System.out.println("[" + listA.get(i).fea +"]-["+listB.get(j).fea +"]");
					
					score = caffeMobile.FFSimilarity(listA.get(i).fea, listB.get(j).fea);
					//1:1 0.45
					//1:n 0.56
					if(score >=threshold){
						if(!(listA.get(i).id.equals(listB.get(j).id)) ){
							if(listA.get(i).id.contains("_") || listB.get(j).id.contains("_")){
								//���»��ߣ��ǲ���֤����								
							}
							else{
//								System.out.println("[" + listA.get(i).id +"]-["+listB.get(j).id +"]:"+score);
								strContent = String.format("[%s]-[%s]:%f\n", listA.get(i).id,listB.get(j).id,score);
								System.out.println(strContent);
								raf.write(strContent.getBytes());
							}
								
						}
					}
				}
			}
			System.out.println("Compare finished.--------------------------");

			
			raf.close();
	      } catch (Exception e) {
	           Log.e("TestFile", "Error on write File.");
          }		
			
		 /*
		    Message message = null;
		    double [] feaA = new double[FACEFEATURESIZE];
		    double [] feaB = new double[FACEFEATURESIZE];
		    double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
		    double [] rectarrayB = new double[PARAMSIZE * MAXFACECOUNT]; //left top width height nQuality
		    int facecountA = 0;
		    int facecountB = 0;
		  
		    //Aͼ�������������ȡ����
		    String filePathA = Environment.getExternalStorageDirectory()+"/FRSDK/008.jpg";
		    
		    facecountA = caffeMobile.FDDetectpath(caffeMobile.DetectHandle[0], filePathA, rectarrayA);
			if(facecountA > 0 )
			{
				caffeMobile.FFFeaExtractPath(caffeMobile.FeatureHandle[0], filePathA, feaA, rectarrayA);
			}
			else
			{
				Log.d(TAG, "A: detect no face");
				return;
			}
				
			//Bͼ�������������ȡ����
			 String filePathB = Environment.getExternalStorageDirectory()+"/FRSDK/009.jpg";

			facecountB = caffeMobile.FDDetectpath(caffeMobile.DetectHandle[0], filePathB, rectarrayB);
			
			if(facecountB > 0 )
			{
				caffeMobile.FFFeaExtractPath(caffeMobile.FeatureHandle[0], filePathB, feaB, rectarrayB);
			}
			else
			{
				Log.d(TAG, "B: detect no face");
				return;
			}
			
			if(facecountA > 0 && facecountB > 0)
			{
				//Date dt= new Date();
				Long t1= System.currentTimeMillis();
				System.out.println("t1="+t1);
				double  score = 0;
				for(int a = 0; a < 10000; a++)
				{
					score = caffeMobile.FFSimilarity(feaA, feaB);
				}
				long t2 = System.currentTimeMillis();
				System.out.println("t2="+t2);
				
				System.out.println("score = :" + score+", 10000 times dt = "+(t2-t1) );
				Log.d("fr=========", "score = :" + score);
				
///				message = Message.obtain(activity.getHandler(),R.id.finish_test_ComputeSimCos,score);
///				message.sendToTarget();
				
			}
			*/
	 }
	
	static byte[] DoubleArrayToByteArray(double[] Value)  
	{  
		int size = Value.length;
	    byte[] byteRet = new byte[8*size];  
		for(int i = 0; i < size; i++)
		{
		    long accum = Double.doubleToRawLongBits(Value[i]);  
		    byteRet[i*8+0] = (byte)(accum & 0xFF);  
		    byteRet[i*8+1] = (byte)((accum>>8) & 0xFF);  
		    byteRet[i*8+2] = (byte)((accum>>16) & 0xFF);  
		    byteRet[i*8+3] = (byte)((accum>>24) & 0xFF);  
		    byteRet[i*8+4] = (byte)((accum>>32) & 0xFF);  
		    byteRet[i*8+5] = (byte)((accum>>40) & 0xFF);  
		    byteRet[i*8+6] = (byte)((accum>>48) & 0xFF);  
		    byteRet[i*8+7] = (byte)((accum>>56) & 0xFF);  
			
		}
	    return byteRet;  
	}
	
	static double[] byteArryToDoubleArray(byte[] Array)     
	{   
		int dSize = Array.length/8;
		double[] doubleRet = new double[dSize];
		for(int i = 0; i < dSize; i++)
		{
		    long accum = 0;  
		    int pos = i*8;
		    accum = Array[pos+0] & 0xFF;  
		    accum |= (long)(Array[pos+1] & 0xFF)<<8;  
		    accum |= (long)(Array[pos+2] & 0xFF)<<16;  
		    accum |= (long)(Array[pos+3] & 0xFF)<<24;  
		    accum |= (long)(Array[pos+4] & 0xFF)<<32;  
		    accum |= (long)(Array[pos+5] & 0xFF)<<40;  
		    accum |= (long)(Array[pos+6] & 0xFF)<<48;  
		    accum |= (long)(Array[pos+7] & 0xFF)<<56; 
		    doubleRet[i] = Double.longBitsToDouble(accum);
		}
		return doubleRet;   
	}  
	
    private void createFileWithByte(byte[] bytes, String fileName) {
        // TODO Auto-generated method stub
        /**
         * ����File�������а����ļ����ڵ�Ŀ¼�Լ��ļ�������
         */
//        File file = new File( (Environment.getExternalStorageDirectory(),
//                fileName);
        File file = new File(fileName);
        // ����FileOutputStream����
        FileOutputStream outputStream = null;
        // ����BufferedOutputStream����
        BufferedOutputStream bufferedOutputStream = null;
        try {
			System.out.println("writing file=["+fileName+"]");
        	
            // ����ļ�������ɾ��
            if (file.exists()) {
                file.delete();
            }
            // ���ļ�ϵͳ�и���·������һ���µĿ��ļ�
            file.createNewFile();
            // ��ȡFileOutputStream����
            outputStream = new FileOutputStream(file);
            // ��ȡBufferedOutputStream����
            bufferedOutputStream = new BufferedOutputStream(outputStream);
            // ���ļ����ڵĻ����������дbyte����
            bufferedOutputStream.write(bytes);
            // ˢ��������������ò��ܹؼ���Ҫ�ǲ�ִ��flush()��������ô�ļ��������ǿյġ�
            bufferedOutputStream.flush();
        } catch (Exception e) {
            // ��ӡ�쳣��Ϣ
            e.printStackTrace();
        } finally {
            // �رմ�����������
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    private byte[] readFileToByte(String fileName) {
        // TODO Auto-generated method stub
        /**
         * ����File�������а����ļ����ڵ�Ŀ¼�Լ��ļ�������
         */
//        File file = new File( (Environment.getExternalStorageDirectory(),
//                fileName);
    	//byte[] bytes  = new byte[];
    	int fileSize = 0;
        File file = new File(fileName);
        // ����FileOutputStream����
        FileInputStream inputStream = null;
        // ����BufferedOutputStream����
        BufferedInputStream bufferedInputStream = null;
        try {
			//System.out.println("reading file=["+fileName+"]");
        	
            // ����ļ�������ɾ��
            if (!file.exists()) {
            	System.out.println("file not exist, file=["+fileName+"]");
                return null;
            }
            
            // ��ȡFileOutputStream����
            inputStream = new FileInputStream(file);
            fileSize = inputStream.available();
            // ��ȡBufferedOutputStream����
            bufferedInputStream = new BufferedInputStream(inputStream);
            // ���ļ����ڵĻ����������дbyte����
            byte[] bytes = new byte[fileSize];
            bufferedInputStream.read(bytes);
            return bytes;
            // ˢ��������������ò��ܹؼ���Ҫ�ǲ�ִ��flush()��������ô�ļ��������ǿյġ�
        } catch (Exception e) {
            // ��ӡ�쳣��Ϣ
            e.printStackTrace();
        } finally {
            // �رմ�����������
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        }
        return null;
    }
    
    /*
   
    public static void WriteTxtFile(String strcontent,String strFilePath)
    {
      //ÿ��д��ʱ��������д
      String strContent=strcontent+"\n";
      try {
           File file = new File(strFilePath);
           if (!file.exists()) {
            Log.d("TestFile", "Create the file:" + strFilePath);
            file.createNewFile();
           }
           RandomAccessFile raf = new RandomAccessFile(file, "rw");
           raf.seek(file.length());
           raf.write(strContent.getBytes());
           raf.close();
      } catch (Exception e) {
           Log.e("TestFile", "Error on write File.");
          }
    }   
     * */
    
	 /*
// ��ȡ��ǰĿ¼�����е�mp4�ļ�  
    public static Vector<String> GetVideoFileName(String fileAbsolutePath) {  
        Vector<String> vecFile = new Vector<String>();  
        File file = new File(fileAbsolutePath);  
        File[] subFile = file.listFiles();  
  
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {  
            // �ж��Ƿ�Ϊ�ļ���  
            if (!subFile[iFileLength].isDirectory()) {  
                String filename = subFile[iFileLength].getName();  
                // �ж��Ƿ�ΪMP4��β  
                if (filename.trim().toLowerCase().endsWith(".mp4")) {  
                    vecFile.add(filename);  
                }  
            }  
        }  
        return vecFile;  
    }  
	  
	  *
	  */
    static class MyFilter implements FilenameFilter{  
        private String type;  
        public MyFilter(String type){  
            this.type = type;  
        }  
        public boolean accept(File dir,String name){  
            return name.endsWith(type);  
        }  
    }  
    
	public void LoadModelFiles()
	{
		String fileAname = "";
		
		targetInfos.clear();
        String fileAbsolutePath = GloableConfig.targetPicPath;
		System.out.println("before LoadModelFiles");
		

        File file = new File(fileAbsolutePath);
        MyFilter fnf = new MyFilter(".mmia");
        File[] subFile = file.listFiles(fnf);
		System.out.println("loading model files");
		sendMsgToMain("����ģ���ļ�");
		if(subFile == null){
        	System.out.println("====, no mmia files found.");
			return;
		}
		sendMsgToMain("�� " + subFile.length + " ��ģ���ļ�.");
		
        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {  
            // �ж��Ƿ�Ϊ�ļ���  
            if (!subFile[iFileLength].isDirectory()) {  
                String filename = subFile[iFileLength].getName();
                subFile[iFileLength].getAbsolutePath();
                
            	if( ((iFileLength+1) % 1000 == 0) ||
            			(iFileLength+1>=subFile.length)){
    		        sendMsgToMain("��������"+(iFileLength+1));                		
            	}
                
                // �ж��Ƿ�ΪMP4��β  
                //if (filename.trim().toLowerCase().endsWith(".mmia")) 
                {
                	fileAname = subFile[iFileLength].getAbsolutePath();
    				System.out.println("loading model file=["+fileAname+"]");
    				TargetInfo tiA = new TargetInfo();
    				tiA.id = new String(subFile[iFileLength].getName().replace(".mmia", ""));
    				tiA.fea = byteArryToDoubleArray(readFileToByte(fileAname));
    				targetInfos.add(tiA);
                }  
            }  
        }  
		sendMsgToMain("����ģ���ļ����.");
	 }
    
	@Override
	public void run() {
		
	    double [] feaA = new double[FACEFEATURESIZE];
	    double [] feaB = new double[FACEFEATURESIZE];
	    double [] rectarrayA = new double[PARAMSIZE * MAXFACECOUNT];	//left top width height nQuality
	    double [] rectarrayB = new double[PARAMSIZE * MAXFACECOUNT]; //left top width height nQuality
	    int facecountA = 0;
	    int facecountB = 0;
	    byte [] BGRA;
	    Bitmap bmp;
		double score = 0.0;
		int sizeA = 0;

		long ifCnt = 1;
		sendMsgToMain("������������߳�...");
	    caffeMobile = new CaffeMobile(activity);
	    while(true){
			if(_targetPicsChanged > 0)
			{
				sendMsgToMain("��鲢���ɡ�����ģ���ļ�...");
				System.out.println("======= target pics changed, reload models");
				makeModelFiles(caffeMobile);
				LoadModelFiles();
				_targetPicsChanged = 0;
				sendMsgToMain("��鲢���ɡ�����ģ���ļ����, ��ʼ������⡢ʶ����.");
				System.out.println("======= target pics changed, reload models finished.");
			}

		    if(informationView.bmpToFDR == null) continue;
		    bmp =  informationView.bmpToFDR;
		    //bmp =  Bitmap.createBitmap(informationView.bmpToFDR);

		    
		    BGRA = getPixelsBGR(bmp);
	    	facecountA = caffeMobile.FDDetect(caffeMobile.DetectHandle[0], BGRA, bmp.getWidth(), bmp.getHeight(), rectarrayA);
			informationView.IAFResult.clear();
			if(facecountA > 0 )
			{
				ifCnt++;
				if(((ifCnt)%100 ) == 0){
					sendMsgToMain("�ѳɹ���⵽����" + ifCnt + "��.");
				}
				
				informationView.bmpToShow = Bitmap.createBitmap(bmp);
				System.out.println("facecountA = :" + facecountA);
				//System.out.println("rectarrayA = :" + rectarrayA.length);
				MatchResult mr = new MatchResult();
				mr.count = 1;
				mr.pos = rectarrayA.clone();
				mr.id = "";
				
				int feaex = caffeMobile.FFFeaExtract(caffeMobile.FeatureHandle[0], BGRA, bmp.getWidth(), bmp.getHeight(), feaA, rectarrayA);
				System.out.println("feaex = :" + feaex);
				score = 0.0;
				sizeA = targetInfos.size();
				for(int i = 0; i<sizeA; i++){
					score = caffeMobile.FFSimilarity(targetInfos.get(i).fea, feaA);
					//System.out.println("--- " + i + " ---" + targetInfos.get(i).id + "--" + score);
					//1:1 0.45
					//1:n 0.56
					if(score >=threshold){
						if(score > mr.score){
							System.out.println("====== " + i + " ---" + targetInfos.get(i).id + "--" + score);
							mr.score = score;
							mr.id = new String(targetInfos.get(i).id);
							mr.matched = 1;
						}
					}
				}
				informationView.IAFResult.add(mr);
			}
			else
			{
				Log.d(TAG, "A: detect no face");
			}
			if(informationView != null) {
	            Message msg = new Message();  
	            msg.what = 100;  
	            informationView.handler.sendMessage(msg);  
				//informationView.invalidate();
			}
			if(_stop > 0) break;
	    }

		//makeModelFiles(caffeMobile);
		//testDiff(caffeMobile);
		/*
		for(int i = 0 ; i <10; i++)
		{
			//Log.d("fr=========", "test:" + i);
			 test(caffeMobile);
		}*/
		// test1100(caffeMobile);
		
		caffeMobile.Destroy();
		Log.d(TAG, "release and finished!");
		
	}

	public void sendMsgToMain(String info){
        Message msg = new Message();  
        msg.what = 10;
        msg.obj = info;
        activity.msgHandler.sendMessage(msg);  		
	}
	
}
