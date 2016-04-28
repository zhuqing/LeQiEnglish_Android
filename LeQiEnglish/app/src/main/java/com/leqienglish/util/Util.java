package com.leqienglish.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

import android.media.AudioFormat;
import android.os.Environment;

public class Util {

	public static final int frequence = 44100; // 录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备
	public static final int channelConfig =  AudioFormat.CHANNEL_IN_STEREO ;
	public static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;

	/**
	 * 
	 * @param plainText
	 * @return
	 */
	public static String Md5(String plainText) {

		StringBuffer buf = new StringBuffer("");
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte b[] = md.digest();

			int i;

			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0)
					i += 256;
				if (i < 16)
					buf.append("0");
				buf.append(Integer.toHexString(i));
			}
		} catch (Exception e) {

		}

		return buf.toString();
	}

	/**
	 * 对url进行转码
	 * 
	 * @param url
	 * @return
	 */
	public static String urlEncode(String url) {
		try {
			url = URLEncoder.encode(url, "utf-8").replaceAll("\\+", "%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return url.replaceAll("%3A", ":").replaceAll("%2F", "/");
	}

	/**
	 * 获取文件路径
	 * 
	 * @return
	 */
	public static String getLearnePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/leqienglish/file/learne";
	}
	
	public static String getImagePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/leqienglish/file/image";
	}

	/**
	 * 按照文件路径 获取文件内容
	 * 
	 * @param path
	 * @return
	 */
	public static String readFile(String path) {

		StringBuffer fileContent = new StringBuffer();
		try {
			File file = new File(path);
			FileInputStream fis = new FileInputStream(file);

			InputStreamReader isr = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(isr);

			int len = -1;
			char[] cha = new char[1024];
			while ((len = br.read(cha)) > 0) {
				fileContent.append(cha, 0, len);
			}

			br.close();
			isr.close();
			fis.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return fileContent.toString();
	}

	/**
	 * 转换时间格式
	 * 
	 * @param value
	 * @return
	 */
	public static Integer getNumber(String value) {
		String[] arr = value.split(":");
		double num = 0;
		if (arr.length == 3) {
			num = Integer.valueOf(arr[0]) * 60 * 60;
			num += Integer.valueOf(arr[1]) * 60;
			num += Double.valueOf(arr[2]);
		} else {
			num = Integer.valueOf(arr[0]) * 60;
			num += Double.valueOf(arr[1]);
		}

		return (int) (num * 1000);
	}
	
	public static void main(String[] args){
		System.out.println(URLEncoder.encode("dasdf adfaf"));
	}
	
	
}
