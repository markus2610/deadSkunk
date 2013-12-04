package com.r0adkll.deadskunk.utils;

import android.content.Context;
import android.os.Environment;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileUtils {

	/**
	 * Constants
	 */
	
	/* Media State Constants */
	public static final int READ_WRITE = 0;
	public static final int READ_ONLY = 1;
	public static final int UNAVAILABLE = 2;
	
	public static final int IO_FAIL = -1;
	public static final int IO_SUCCESS = 0;
	
	
	
	
	/**
	 * Check the state of the external media (i.e. SDCard) on whether 
	 * it is read/write, read only, or unavailable
	 * 
	 * @return 	the corresponding constant to the media state(RW/RO/Unavailable)
	 */
	public static int checkMediaState(){
		int mediastate = UNAVAILABLE;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mediastate = READ_WRITE;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mediastate = READ_ONLY;
		} else {
		    // Something else is wrong. It may2 nor write
		    mediastate = UNAVAILABLE;
		}
		
		return mediastate;
	}
	
	/**
	 * Write a file, passed as a byte array, to the internal storage
	 * with the passed write mode
	 * 
	 * @param ctx			the application context
	 * @param FILENAME		the name of the file to save
	 * @param data			the data to save to the file
	 * @param writemode		the mode to write the data in.
	 * @return				the Success or Failure of writing the file
	 */
	public static int writeFileToInternal(Context ctx, String FILENAME, byte[] data , int writemode){
		int code = IO_FAIL;		
		try {
			
			FileOutputStream fos = ctx.openFileOutput(FILENAME, writemode);
			fos.write(data);
			fos.close();
			code = IO_SUCCESS;			
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
	 * This will read a file from the internal storage
	 * and return the contents in a byte[] buffer.
	 * 
	 * @param ctx			the application context
	 * @param FILENAME		the name of the file
	 * @return				the byte array of data in the file
	 */
	public static byte[] readFileFromInternal(Context ctx, String FILENAME){
		byte[] buffer;
		
		try {
			FileInputStream fis = ctx.openFileInput(FILENAME);
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
	 * Write a file to the external drive (sdcard)
	 * 
	 * 
	 * @param ctx			the application context
	 * @param FILENAME		the file name/path
	 * @param data			the file data
	 * @param writemode		the write mode
	 * @return				the IO result code
	 */
	public static int writeFileToExternal(Context ctx, String FILENAME, byte[] data, int writemode){
		int code = IO_FAIL;
		// Verify Media State
		int state = checkMediaState();
		if(state == READ_WRITE){
			
			// Get external directory
			File dir = ctx.getExternalFilesDir(null);
			
			// get file to write
			try {
				// Create file output stream
				FileOutputStream fos = new FileOutputStream(new File(dir.getAbsolutePath().concat(FILENAME)));
				fos.write(data);
				fos.close();				
				code = IO_SUCCESS;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return code;
	}
	
	/**
	 * Write a file to the external drive (sdcard)
	 * 
	 * 
	 * @param ctx			the application context
	 * @param FILENAME		the file name/path
	 * @param data			the file data
	 * @param writemode		the write mode
	 * @return				the IO result code
	 */
	public static byte[] readFileToExternal(Context ctx, String FILENAME){
		
		// Verify Media State
		int state = checkMediaState();
		if(state == READ_WRITE){
			
			// Get external directory
			File dir = ctx.getExternalFilesDir(null);
			
			// get file to write
			try {
				// Create file output stream
				FileInputStream fis = new FileInputStream(new File(dir.getAbsolutePath().concat(FILENAME)));
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer);
				fis.close();
				
				// Return the raw data
				return buffer;				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	/**
	 * Write a serializable object to the cache
	 * @param ctx
	 * @param filepath
	 * @param obj
	 * @return
	 */
	public static int writeObjectToInternal(Context ctx, String filename, Serializable obj){
		int code = FileUtils.IO_FAIL;

		try {
			File filesDir = ctx.getFilesDir();
			File output = new File(filesDir, filename);
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
			File filesDir = ctx.getFilesDir();
			File input = new File(filesDir, filename);
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
	 * Delete a directory recursively 
	 * @param dir	the directory to delete
	 */
	public static boolean deleteDirectory(File dir){
		
		// List all files in dir
		File[] files = dir.listFiles();
		if(files!=null){
			for(File f: files){
				
				// Check to see if file is a directory or not
				if(f.isDirectory()){
					// Recursively delete directories
					deleteDirectory(f);
				}else{
					// Delete the file
					f.delete();
				}
			}
			// delete the directory itself
			return dir.delete();
		}
		return false;
	}
	
	
	/**
	 * Hash a string in md5
	 * 
	 * @param in	the string to hash
	 * @return		the md5 hashed string
	 */
	public static String md5(String in) {
	    MessageDigest digest;
	    try {
	        digest = MessageDigest.getInstance("MD5");
	        digest.reset();        
	        digest.update(in.getBytes());
	        byte[] a = digest.digest();
	        int len = a.length;
	        StringBuilder sb = new StringBuilder(len << 1);
	        for (int i = 0; i < len; i++) {
	            sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
	            sb.append(Character.forDigit(a[i] & 0x0f, 16));
	        }
	        return sb.toString();
	    } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
	    return null;
	}
}
