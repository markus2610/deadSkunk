package com.r0adkll.deadskunk.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


import android.content.Context;

/**
 * This manages all file interactions within
 * cache memory
 * @author r0adkll
 *
 */
public class CacheTool {

	/**
	 * Variables
	 */
		
	/**
	 * Write a file to the cache
	 * 
	 * @param filepath		the filename/path
	 * @param data			the file data
	 */
	public static int write(Context ctx, String filepath, byte[] data){
		int code = FileUtils.IO_FAIL;		
		try {
			
			File cachedir = ctx.getCacheDir();
			FileOutputStream fos = new FileOutputStream(cachedir.getPath().concat(filepath), false);
			fos.write(data);
			fos.close();
			code = FileUtils.IO_SUCCESS;			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * Read a file from the cache
	 * @param filepath	the file to read
	 * @return		the file data in a byte array, null if failure
	 */
	public static byte[] read(Context ctx, String filepath){
		byte[] buffer;
		
		try { 
			File cachedir = ctx.getCacheDir();
			FileInputStream fis = new FileInputStream(cachedir.getPath().concat(filepath));
			buffer = new byte[fis.available()];			
			fis.read(buffer);
			fis.close();			
			return buffer;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;	
	}
	
	/**
	 * Delete a file from the cache
	 * @param filepath   the file to delete
	 * @return			 true if deleted, false otherwise
	 */
	public static boolean delete(Context ctx, String filepath){
		
		// delete the file
		File _cacheDir = ctx.getCacheDir();
		File fileToDel = new File(_cacheDir.getAbsolutePath().concat(filepath));
		boolean result =  fileToDel.delete();
		
		return result;
	}
	
	/**
	 * Completely clear out the cache
	 * @return
	 */
	public static boolean clear(Context ctx){
		
		// Clear out all the directories
		File _cacheDir = ctx.getCacheDir();
		return FileUtils.deleteDirectory(_cacheDir);		
	}
	
	/**
	 * Get the used size of bytes in the cache directory
	 * @return
	 */
	public long getCacheSize(Context ctx){
		File _cacheDir = ctx.getCacheDir();
		return _cacheDir.getUsableSpace() - _cacheDir.getFreeSpace();
	}
	
}
