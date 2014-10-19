package com.r0adkll.deadskunk.utils;

import android.content.Context;
import android.os.Environment;
import timber.log.Timber;

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
     * Dump Data straight to the SDCard
     *
     * @param ctx           the application context
     * @param filename      the dump filename
     * @param data          the data to dump
     * @return              the return
     */
    public static int crapToDisk(Context ctx, String filename, byte[] data){
        int code = IO_FAIL;

        File dir = Environment.getExternalStorageDirectory();
        File output = new File(dir, filename);
        try {
            FileOutputStream fos = new FileOutputStream(output);
            try {
                fos.write(data);
                code = IO_SUCCESS;
            } catch (IOException e) {
                code = IO_FAIL;
            } finally {
                fos.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return code;
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
     * Copy a file from it's source input to the specified output
     * file if it can.
     *
     * @param source        the input file to copy
     * @param output        the output destination
     * @return              true if successful, false otherwise
     */
    public static boolean copy(File source, File output){

        // Check to see if output exists
        if(output.exists() && output.canWrite()){
            // Delete the existing file, and create a new one
            if(output.delete()) {
                try {
                    output.createNewFile();
                } catch (IOException e) {
                    Timber.e(e, "Error creating new output file for copying");
                }
            }
        }else if(!output.exists()){
            try {
                output.createNewFile();
            } catch (IOException e) {
                Timber.e(e, "Error creating new output file for copying");
            }
        }

        // now that we have performed a prelimanary check on the output, time to copy
        if(source.exists() && source.canRead()){

            try {
                FileInputStream fis = new FileInputStream(source);
                FileOutputStream fos = new FileOutputStream(output);
                byte[] buffer = new byte[1024];

                int len=0;
                while((len=fis.read(buffer)) > 0){
                    fos.write(buffer, 0, len);
                }

                fis.close();
                fos.close();

                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

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
	    } catch (NoSuchAlgorithmException e) { Timber.e("Error: " + e.getLocalizedMessage()); }
	    return null;
	}
}
