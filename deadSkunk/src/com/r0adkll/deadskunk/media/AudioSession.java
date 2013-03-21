package com.r0adkll.deadskunk.media;

import java.io.File;

import android.net.Uri;

/**
 * Recorded Audio Session
 * @author drew.heavner
 *
 */
public class AudioSession{
	
	/**
	 * Variables
	 */
	private String audioFile;		
	private long length;
	
	public AudioSession(String file){
		this.audioFile = file;
	}
	
	public String getAudioFilePath(){
		return audioFile;
	}
	
	public long getLength(){
		return length;
	}
	
	public Uri getFileURI(){
		return Uri.fromFile(new File(audioFile));
	}
	
}