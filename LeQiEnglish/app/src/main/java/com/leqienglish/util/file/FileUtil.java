package com.leqienglish.util.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.leqienglish.util.LOGGER;
import com.leqienglish.util.Util;

public class FileUtil {
	public static LOGGER logger = new LOGGER(FileUtil.class);
	/**
	 * 从连接中获取文件名称
	 * @param httpURL
	 * @return
	 */
	public static String getFileName(String httpURL){
		if(httpURL==null){
			return null;
		}
		int index =httpURL.lastIndexOf("/");
		return httpURL.substring(index+1,httpURL.length());
	}
	
	/**
	 * 判断IconPath是否可用
	 * @param iconPath
	 * @return
	 */
	public static boolean canUserIconPath(String iconPath){
		if(iconPath==null){
			return false;
		}
		
		if(iconPath.length()==0){
			return false;
		}
		logger.v(iconPath);
		return iconPath.startsWith("http");
	}
	

	/**
	 * 解压压缩文件
	 * 
	 * @param zipfile
	 *            压缩文件的位置
	 * @param destDir
	 *            解压后的位置
	 */
	public static void unZip(String zipfile, String destDir) {
		logger.v("unZip");
		destDir = destDir.endsWith("/") ? destDir : destDir + "/";

		byte b[] = new byte[1024];

		int length;
		ZipFile zipFile;

		try {

			zipFile = new ZipFile(new File(zipfile));

			Enumeration enumeration = zipFile.entries();

			ZipEntry zipEntry = null;
			while (enumeration.hasMoreElements()) {

				zipEntry = (ZipEntry) enumeration.nextElement();

				File loadFile = new File(destDir + zipEntry.getName());

				if (zipEntry.isDirectory()) {

					// 这段都可以不要，因为每次都貌似从最底层开始遍历的

					loadFile.mkdirs();

				} else {

					if (!loadFile.getParentFile().exists())

						loadFile.getParentFile().mkdirs();

					OutputStream outputStream = new FileOutputStream(loadFile);

					InputStream inputStream = zipFile.getInputStream(zipEntry);

					while ((length = inputStream.read(b)) > 0)

						outputStream.write(b, 0, length);

				}

			}

			System.out.println(" 文件解压成功 ");

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}
	}

	/**
	 * 计算文件的路径
	 * 
	 * @return
	 */
	public static String getFilePath(String httpUrl) {
		String[] arr = httpUrl.split("/");
		String fileName = arr[arr.length - 1];
	//	String folderName = fileName.substring(0, fileName.length() - 3);
		//fileName = folderName + ".zip";
		// Context.
		File file = new File(Util.getLearnePath());
		if (!file.isDirectory()) {
			file.mkdirs();
		}

		return file.getAbsolutePath() + "/" + fileName;
	}
	
	public static String getDirFilePath(String filePath){
		if(!filePath.endsWith(".lq")){
			return filePath;
		}
		
		filePath = filePath.substring(0, filePath.length()-3);
		return filePath;
	}
	
	/**
	 * 获取文件结尾
	 * 
	 * @return
	 */
	public static String getFileEnd(String source) {
		int lastindex = source.lastIndexOf(".");
		return source.substring(lastindex + 1, source.length());
	}
	
	public static String removeStart(String source,String start){
		int index = source.indexOf(start);
		return source.substring(index, source.length());
	}
	
}
