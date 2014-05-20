/*
 * MIT License (MIT)
 *
 * Copyright (c) 2014 Drew Heavner
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.r0adkll.deadskunk.media;

import android.media.MediaRecorder;
import com.r0adkll.deadskunk.utils.Utils;

import java.io.IOException;

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
