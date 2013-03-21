package com.r0adkll.deadskunk.media;

import java.io.IOException;

import com.r0adkll.deadskunk.utils.Utils;


import android.media.MediaRecorder;

/**
 * The Audio Recording class 
 * 
 * THERE IS NO PAUSE, FUCKIN' A.
 * 
 * 
 * @author drew.heavner
 *
 */
public class AudioRecorder {
	private static final String TAG = "AUDIO_RECORDER";
	
	/***************************
	 * Variables
	 */

	private MediaRecorder _media;
	private boolean _sessionInProgress = false;
	private AudioSession _currSessionFile = null;
	
	/**
	 * Constructor
	 */
	public AudioRecorder(){	}
	

	/**
	 * Start Recording audio to teh specified output file name
	 * @param outputFile	the path to the file you want to save the audio as
	 */
	public void startRecording(AudioSession session){
		if(!_sessionInProgress){
			// Set teh Current session object
			_currSessionFile = session;
			
			// Initialize the Media Recorder API object
			_media = new MediaRecorder();
			_media.setOnErrorListener(new AudioErrorListener());
			_media.setOnInfoListener(new AudioInfoListener());
			_media.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
			_media.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
			_media.setAudioSamplingRate(44100);
			_media.setAudioEncodingBitRate(16);
			_media.setAudioChannels(2);
			_media.setOutputFile(_currSessionFile.getAudioFilePath());
			_media.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

			// Prepare recorder
			try {

				// Preparing
				_media.prepare();

				// Success, now start recording
				_media.start();

				// Flag
				_sessionInProgress = true;

			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Stop the current recording session
	 * 
	 * @return  	returns the path to the file of the recording session you are trying to end
	 */
	public AudioSession stopRecording(){
		if(_sessionInProgress){
			
			// Stop the audio recorder
			_media.stop();
			
			// Release the audio recorder to free up used resources and codecs 
			_media.release();
			
			// Nullify media recorder to finally releave all audio resources
			_media = null;
			
			// Set Flag
			_sessionInProgress = false;	
			
			// Return session file path
			return _currSessionFile;
		}
		return null;
	}

	/**
	 * Return whether or not there is a current recording 
	 * session in progress
	 * @return		true if currently recording, false otherwise
	 */
	public boolean isSessionLive(){
		
		return _sessionInProgress;
	}
	
	/********************************************************
	 * Inner Classes and Interfaces
	 * 
	 */
	
	
	
	/**
	 * This is the error listener for the audio recorder if an error
	 * occurs during the recording process
	 * 
	 * @author drew.heavner
	 *
	 */
	static class AudioErrorListener implements MediaRecorder.OnErrorListener{
		@Override
		public void onError(MediaRecorder mr, int what, int extra) {
			Utils.log(TAG, "Recording Error[" + what + ", " + extra + "]");
		}		
	}
	
	/**
	 * This is the info listener for the media recorder
	 * 
	 * @author drew.heavner
	 *
	 */
	static class AudioInfoListener implements MediaRecorder.OnInfoListener{
		@Override
		public void onInfo(MediaRecorder mr, int what, int extra) {
			Utils.log(TAG, "Recorder Info[" + what + ", " + extra + "]");
		}		
	}
	
	

}
