package com.r0adkll.deadskunk.utils;

import android.content.Context;

import java.io.*;

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
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * Write a serializable object to the cache
	 * @param ctx
	 * @param filepath
	 * @param obj
	 * @return
	 */
	public static int writeObject(Context ctx, String filename, Object obj){
		int code = FileUtils.IO_FAIL;

		try {
			File cacheDir = ctx.getCacheDir();
			File output = new File(cacheDir, filename);
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(output));
			oos.writeObject(obj);
			oos.close();
			code = FileUtils.IO_SUCCESS;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return code;
	}
	
	/**
	 * Read an object from disk that was written via serialization
	 * @param ctx
	 * @param filename
	 * @return
	 */
	public static Object readObject(Context ctx, String filename){
		try {
			File cacheDir = ctx.getCacheDir();
			File input = new File(cacheDir, filename);
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(input));
			Object src = ois.readObject();
			ois.close();
			return src;
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return null;
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
