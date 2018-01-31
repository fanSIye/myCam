package com.example.mycam;
import java.util.UUID;  

import android.os.Environment;
public class GloableConfig {
//  Jedis jedis = new Jedis("192.168.1.60",6379);
//  Jedis jedis = new Jedis("10.8.0.7",6379);
	//static public String redisServerIP = "10.8.0.7";
	static public String redisServerIP = "192.168.1.60";
	static public int redisServerPort = 6379;
	
//	static public String redisServerIP = "112.74.182.57";
//	static public int redisServerPort = 5000;
	static public String redisServerPwd = "Allsee009";
	static public String targetPicPath = Environment.getExternalStorageDirectory()+"/FRSDK/t1";;
	private static String redisKey = "";
	public GloableConfig(){
		
		
	}

	private static void makeUUID() {  
		if(redisKey.equals(""))
		{
			UUID uuid = UUID.randomUUID(); 
			redisKey = uuid.toString();
			redisKey = redisKey.replace("-","");
		}
	}
	
	public String getRedisKey() {
		makeUUID();
		return redisKey;		
	}
}
