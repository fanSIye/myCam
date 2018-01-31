package com.example.mycam;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//import com.arcsoft.facedetection.AFD_FSDKFace;
//import com.arcsoft.facetracking.*;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import redis.clients.jedis.Jedis;

@SuppressLint("DefaultLocale")
public class InformationView extends View {
	//public List<AFD_FSDKFace> AFDresult = new ArrayList<AFD_FSDKFace>();
	//public List<AFT_FSDKFace> AFTresult = new ArrayList<AFT_FSDKFace>();
	Context context;
//	public List<double[]> IAFResult = new ArrayList<double[]>();
	public List<MatchResult> IAFResult = new ArrayList<MatchResult>();
	
	public Bitmap bmpToFDR = null;
	public Bitmap bmpToShow = null;
	public int orgBmpWidth = 0, orgBmpHeight = 0;
	Paint paint = new Paint();
	Paint paintFont = new Paint();
	Paint paintFont2 = new Paint();
	int iTmp = 0;
	public String redisKey = "";
    ///Jedis jedis = new Jedis(GloableConfig.redisServerIP,GloableConfig.redisServerPort);

    String redisText,displayText = "*****";
    int redisStop = 0;
    Bitmap  captureBitmap;
    Bitmap  targetBitmap;
    
    Bitmap latestFace = null;
	Rect fr1 = new Rect(),fr2;
	FRSDKThread frThread = null;
	
	public void SetFRSDKThread(FRSDKThread t) {
		frThread = t;		
	}
	
	public InformationView(Context context) {
		super(context);
		this.context = context;
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(4F);
		paint.setTextSize(40);
        paint.setColor(Color.rgb(255, 232, 0));
        
//        paintFont.setStyle(Paint.Style.FILL_AND_STROKE);
        paintFont.setStyle(Paint.Style.FILL);
        paintFont.setStrokeWidth(1.5F);
        paintFont.setTextSize(40);
        paintFont.setColor(Color.rgb(64, 64, 64));
        paintFont.setTextAlign(Paint.Align.CENTER);
        
        paintFont2.setStyle(Paint.Style.FILL_AND_STROKE);
        paintFont2.setStrokeWidth(3F);
        paintFont2.setTextSize(70);
        paintFont2.setColor(Color.rgb(230, 230, 230));
        paintFont2.setTextAlign(Paint.Align.CENTER);
        
        GloableConfig gloableConfig = new GloableConfig();
    	redisKey = gloableConfig.getRedisKey();
//    	faceBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.face);
//    	faceOkBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.faceok);
    	captureBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.capture);
    	targetBitmap = BitmapFactory.decodeResource(this.getContext().getResources(), R.drawable.target);

		fr1.left = 0;
		fr1.top = 0;
		fr1.right = captureBitmap.getWidth() - 1;
		fr1.bottom = captureBitmap.getHeight() - 1;
		
		int faceWidth = this.getWidth()/2;
		int dy = (this.getHeight() - fr1.height()) / 3;
		fr2 = new Rect(fr1);
		fr2.right = faceWidth;
		fr2.bottom = faceWidth;
		
		fr2.left += faceWidth/2;
		fr2.right += faceWidth/2;
		fr2.top += dy;
		fr2.bottom += dy;
//		Log.wtf("==============fr1", String.valueOf(fr1.left) + ":" + String.valueOf(fr1.top) + "-" +
//				String.valueOf(fr1.right) + ":" + String.valueOf(fr1.bottom));
//		Log.wtf("==============fr2", String.valueOf(fr2.left) + ":" + String.valueOf(fr2.top) + "-" +
//				String.valueOf(fr2.right) + ":" + String.valueOf(fr2.bottom));    	
        //new Thread(jedisConnect).start();
        //new Thread(jedisRead).start();
        
	}

	@Override
	protected void onDetachedFromWindow(){
		super.onDetachedFromWindow();
		redisStop = 1;
        new Thread(jedisDisconnect).start();
		
	}
	
	private boolean checkPosition(Rect r1, Rect r2){
		boolean vr = false;
		int dw = (int) Math.abs(r1.width()*0.6 - r2.width());
		int dcx = (r1.left + r1.right)/2 - (r2.left + r2.right)/2;
		int dcy = (r1.top + r1.bottom)/2 - (r2.top + r2.bottom)/2;
		int dr = (int) Math.sqrt(dcx * dcx + dcy * dcy);
		
//		Log.wtf("==============check", String.valueOf(dr) + ":" + String.valueOf(dw));
		
		if((dw < 90) && (dr < 90)) {
			vr = true;
		}
				
		return vr;
	}
	
	private boolean drawCheckLable(Canvas canvas, Rect r1, Rect r2){
		boolean vr = false;
		if(checkPosition(r1,r2)){
			//canvas.drawBitmap(faceOkBitmap, fr1,fr2, paint);
		}else{
			//canvas.drawBitmap(faceBitmap, fr1,fr2, paint);
		}
		//canvas.drawRect(r2, paint);
		
		return vr;
	}
	private String showInputDialog() {
		String ret;
	    final EditText editText = new EditText(context);
	    AlertDialog.Builder inputDialog = 
	        new AlertDialog.Builder(context);
	    inputDialog.setTitle("输入名称：").setView(editText);
	    inputDialog.setPositiveButton("确定", 
	        new DialogInterface.OnClickListener() {
	        @Override
	        public void onClick(DialogInterface dialog, int which) {
	            Toast.makeText(context,
	            editText.getText().toString(), 
	            Toast.LENGTH_SHORT).show();
	        }
	    }).show();
    	ret = editText.getText().toString(); 
	    return ret;
	}
	
	public int saveCapture(String fni){
		int ret = 0;
		Bitmap bmp = Bitmap.createBitmap(latestFace);
		String fn = GloableConfig.targetPicPath + "/" + fni +".jpg";
		String fn1 = GloableConfig.targetPicPath + "/" + fni +".mmia";
  		System.out.println("======== try to save :" + fn);
  		File f = new File(fn);
  		File f2 = new File(fn1);
  		
  
  		if (f.exists()) {
   			f.delete();
  		}
  		if (f2.exists()) {
   			f2.delete();
  		}
  		try {
  			FileOutputStream out = new FileOutputStream(f);
  			bmp.compress(Bitmap.CompressFormat.JPEG, 95, out);
  			out.flush();
  			out.close();
  	  		System.out.println("======== save successed:" + fn);
  	  		if(frThread!=null){
  	  			frThread._targetPicsChanged = 1;  	  		
  	  		}
  		} catch (FileNotFoundException e) {
  			e.printStackTrace();
  		} catch (IOException e) {
  			e.printStackTrace();
  		}
		
		
		
		return ret;
	}
	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		Rect tr = new Rect();
//		paint.setColor(Color.YELLOW);
		//drawCheckLable(canvas, tr, fr2);
		int faceWidth = this.getWidth()/2;
		int dy = (this.getHeight() - fr1.height()) / 3;
		fr2 = new Rect(fr1);
		fr2.right = faceWidth;
		fr2.bottom = faceWidth;
		
		fr2.left += faceWidth/2;
		fr2.right += faceWidth/2;
		fr2.top += dy;
		fr2.bottom += dy;
		
		//canvas.drawBitmap(faceOkBitmap, fr1,fr2, paint);
		System.out.println("-----------------------------------");
		if(bmpToShow != null){
			Rect r1 = new Rect(0,0,bmpToShow.getWidth(), bmpToShow.getHeight());
			Rect r2 = new Rect(0,500,0+bmpToShow.getWidth(), 500+bmpToShow.getHeight());
			//canvas.drawBitmap(bmpToShow, r1, r2, paint);
		}
		   int xxx =  1050;
	       int yyy =  240;
	       int rrr = 90;
	       Paint p1 = new Paint();
	       p1.setAntiAlias(true);
	       p1.setColor(Color.GRAY);
	       canvas.drawCircle(xxx, yyy, rrr, p1);
	       
		if(IAFResult.size()>0){
			for (MatchResult mr : IAFResult) {
				String id = mr.id;
				double score = mr.score;
				double[] result = mr.pos;			
				tr.left = (int) result[0];
				tr.top = (int) result[1];
				tr.right = (int) result[2]+(int) result[0];
				tr.bottom = (int) result[3]+(int) result[1];
				
				latestFace = Bitmap.createBitmap(bmpToShow,	(int) result[0],(int) result[1],(int) result[2],(int) result[3]);
				Rect r2 = new Rect(700,140,900, 340);
				canvas.drawBitmap(bmpToShow, tr, r2, paint);
				canvas.drawRect(tr, paint);
				canvas.drawText(id, 1050, 450, paintFont);//show target id			
				
				//show target pic
				if(!id.equals("")){
					String targetPath = GloableConfig.targetPicPath + "/" + id + ".jpg";
					Bitmap target = BitmapFactory.decodeFile(targetPath);
					tr.left = 0;
					tr.top = 0;
					tr.right = target.getWidth();
					tr.bottom = target.getHeight();
					r2 = new Rect(1200,140,1400, 340);
					canvas.drawBitmap(target, tr, r2, paint);
				}
				else{
					{
						tr.left = 0;
						tr.top = 0;
						tr.right = targetBitmap.getWidth();
						tr.bottom = targetBitmap.getHeight();
						r2 = new Rect(1200,140,1400, 340);
						canvas.drawBitmap(targetBitmap, tr, r2, paint);
					}
				}
				//draw score
				if(score > 0.01){
				       canvas.drawText(String.format("%2d%%", (int)(score*100)), 
				    		   xxx, yyy+25, paintFont2);//show target id			
				}
				
			}
		}
		else
		{
			tr.left = 0;
			tr.top = 0;
			tr.right = captureBitmap.getWidth();
			tr.bottom = captureBitmap.getHeight();
			Rect r2 = new Rect(700,140,900, 340);
			canvas.drawBitmap(captureBitmap, tr, r2, paint);

			tr.left = 0;
			tr.top = 0;
			tr.right = targetBitmap.getWidth();
			tr.bottom = targetBitmap.getHeight();
			r2 = new Rect(1200,140,1400, 340);
			canvas.drawBitmap(targetBitmap, tr, r2, paint);
		}
		
		/*
		for (AFD_FSDKFace face : AFDresult) {
			Rect r = face.getRect();
			tr.left = r.top;
			tr.top = orgBmpWidth-r.right;
			tr.right = r.bottom;
			tr.bottom = orgBmpWidth-r.left;
			
			//for L-R mirror
			tr.left = orgBmpHeight - tr.left;
			tr.right = orgBmpHeight - tr.right;
			
			tr.top = tr.top * this.getHeight()/orgBmpWidth;
			tr.bottom = tr.bottom * this.getHeight()/orgBmpWidth;
			tr.left = tr.left * this.getWidth()/orgBmpHeight;
			tr.right = tr.right * this.getWidth()/orgBmpHeight;
			
			iTmp = tr.left;
			if(tr.left > tr.right){
				tr.left = tr.right;
				tr.right = iTmp;
			}
			iTmp = tr.top;
			if(tr.top > tr.bottom){
				tr.top = tr.bottom;
				tr.bottom = iTmp;
			}
			
			
			
			//tr.left = lll;
			//tr.right = rrr;
//			tr.left = r.left/4+100;
//			tr.top = r.top/4+100;
//			tr.right = r.right/4+100;
//			tr.bottom = r.bottom/4+100;
//			Rect r1 = new Rect(0,0,bmpToShow.getWidth(), bmpToShow.getHeight());
//			Rect r2 = new Rect(100,100,100+bmpToShow.getWidth()/4, 100+bmpToShow.getHeight()/4);
//			if(bmpToShow != null)canvas.drawBitmap(bmpToShow, r1, r2, paint);

//			RectF trF = new RectF();
//			trF.left = tr.left;
//			trF.top = tr.top;
//			trF.right = tr.right;
//			trF.bottom = tr.bottom;
//			canvas.drawRoundRect(trF, 5, 5, paint);
//			canvas.drawText("left:"+r.left + ",top:" + r.top + ", right: "+r.right + ", bottom:" + r.bottom + 
//					":" + orgBmpWidth + " x " + orgBmpHeight, 
//					10, 50, paint);
//			canvas.drawText("left:"+tr.left + ",top:" + tr.top + ", right: "+tr.right + ", bottom:" + tr.bottom +
//					":" + orgBmpWidth + " x " + orgBmpHeight + "{}" + this.getWidth() + " x " + this.getHeight(), 
//					10, 100, paint);
			//canvas.drawText(displayText, tr.left, tr.top+50, paintFont);

			//显示脸部引导图
			/////drawCheckLable(canvas, tr, fr2);
			
			canvas.drawRect(tr, paint);
			String [] temp = displayText.split(",");
			if(temp.length > 6){
				if(temp[6].equals("1"))
				{
//					String s = String.format("%s(%2d%%,%dX%d)", temp[4],(int)(Float.parseFloat(temp[5])*100),
//							tr.width(),tr.height());
					String s = String.format("%s(%2d%%)", temp[4],(int)(Float.parseFloat(temp[5])*100));
					canvas.drawText(s, tr.left, tr.top-15, paintFont);
				}
			}
			//canvas.drawText("800,800", 800, 800, paint);
			//canvas.drawText("800,1600", 800, 1600, paint);
			//Log.d("com.arcsoft", "Face:" + face.toString());
		}
		
		*/
		//显示脸部引导图
		////drawCheckLable(canvas, tr, fr2);
		//Log.i("xxxxxxxxxx", "drawing...");
		super.onDraw(canvas);
	}
	public void showSmallPic(byte[] data) {
	}

	//====================================
    Runnable jedisConnect = new Runnable() {    	  
        @Override  
        public void run() {  
        ///	jedis.connect();
        ///	jedis.auth(GloableConfig.redisServerPwd);
        }
    };
    
    Runnable jedisDisconnect = new Runnable() {    	  
        @Override  
        public void run() {  
        ///	jedis.disconnect();
        }  
    };
    
	Runnable jedisRead = new Runnable() {    	  
        @Override  
        public void run() {
        	int nRCount = 0;
        	while(true)
        	{
        		if(redisStop == 1) break;
                try{
                	Thread.sleep(10, 0);
                    redisText = "";///jedis.rpop(redisKey);// get("nachos");
                    //redisText = jedis.rpop("wtf");// get("nachos");
                    if(redisText != null){
                        nRCount = 0;                    	
                    	displayText = redisText;
                    }
                }
                catch(Exception e){
                	Log.v("Redis", e.toString());
                }

                if(nRCount > 200)
                {
                	displayText = "";
                }
                else
                {
                    nRCount++;
                }
        	}
        }  
    };    
    public Handler handler = new Handler() {  
        @Override  
        public void handleMessage(Message msg) {  
            if (msg.what == 100) {
            	invalidate();
    //            stateText.setText("completed");  
            }  
        }  
    };  

}
