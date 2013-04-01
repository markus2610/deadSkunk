package com.r0adkll.deadskunk.media;

import java.io.IOException;

import com.r0adkll.deadskunk.utils.Utils;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

/**
 * This class manages playing Audio Session Objects
 * @author drew.heavner
 *
 */
public class AudioPlayer {
	private static final String TAG = "AUDIO_PLAYER";

	/**
	 * CONSTANTS
	 */
	public static final int IDLE = 0;
	public static final int INITIALIZED = 1;
	public static final int PREPARED = 2;
	public static final int STARTED = 3;
	public static final int PAUSED = 4;
	public static final int STOPPED = 5;
	public static final int COMPLETED = 6;
	public static final int END = 7;
	public static final int ERROR = 8;

	/**
	 * Variables
	 */
	private Context _ctx;
	private MediaPlayer _player;
	private AudioSession _currentSession;

	private int STATE = IDLE;

	/**
	 * Empty Constructor
	 */
	public AudioPlayer(Context ctx){_ctx = ctx;}


	/**************************************************************************************************************
	 * 
	 * 	MEDIA PLAYER SESSION METHODS.
	 *  - used to prepare audio sessions, play, pause, stop, release(destroy), seek
	 * 
	 */


	/**
	 * Prepare a new Audio Session 
	 * 
	 * This will prepare the MediaPlayer instance to play the audio from
	 * the session object and put hte state of this machine in the 'PREPARED' state
	 * from which you can call 'play()' to start playing the audio
	 * 
	 * If you have previously prepared another audio session, you must first call 'release()' before
	 * calling this method again.
	 * 
	 * @param session	the audio session to play
	 */
	public void prepareAudioSession(AudioSession session, MediaPlayer.OnPreparedListener listener){
		if(_player == null){

			// Set Current Session
			_currentSession = session;

			// Construct Media Player
			_player = new MediaPlayer();
			if(session.getAudioFilePath().contains("http://") || session.getAudioFilePath().contains("https://"))
				_player.setAudioStreamType(AudioManager.STREAM_MUSIC);

			// Set listeners
			_player.setOnPreparedListener(listener);
			_player.setOnCompletionListener(new AudioCompletionListener());
			_player.setOnSeekCompleteListener(new AudioSeekCompleteListener());
			_player.setOnInfoListener(new AudioInfoListener());
			_player.setOnErrorListener(new AudioErrorListener());

			try {					
				// IDLE STATE

				// Set Player data Source
				if(session.getAudioFilePath().contains("http://") || session.getAudioFilePath().contains("https://"))
					_player.setDataSource(_ctx, Uri.parse(session.getAudioFilePath()));
				else
					_player.setDataSource(session.getAudioFilePath());

				// INITIALIZED STATE
				STATE = INITIALIZED;

				// Prepare the Media Player
				_player.prepareAsync();			

				// PREPARED STATE
				STATE = PREPARED;

			} catch (IllegalArgumentException e) {
				release();
				e.printStackTrace();
			} catch (SecurityException e) {
				release();
				e.printStackTrace();
			} catch (IllegalStateException e) {
				release();
				e.printStackTrace();
			} catch (IOException e) {
				release();
				e.printStackTrace();
			}
		}
	}

	/**
	 * Prepare a new Audio Session 
	 * 
	 * This will prepare the MediaPlayer instance to play the audio from
	 * the session object and put hte state of this machine in the 'PREPARED' state
	 * from which you can call 'play()' to start playing the audio
	 * 
	 * If you have previously prepared another audio session, you must first call 'release()' before
	 * calling this method again.
	 * 
	 * @param session	the audio session to play
	 * @param completeListener the audio completion listener
	 */
	public void prepareAudioSession(AudioSession session, 
			MediaPlayer.OnPreparedListener listener, 
			MediaPlayer.OnCompletionListener completeListener){

		if(_player == null){

			// Set Current Session
			_currentSession = session;

			// Construct Media Player
			_player = new MediaPlayer();
			if(session.getAudioFilePath().contains("http://") || session.getAudioFilePath().contains("https://"))
				_player.setAudioStreamType(AudioManager.STREAM_MUSIC);

			// Set listeners
			_player.setOnPreparedListener(listener);
			_player.setOnCompletionListener(completeListener);
			_player.setOnSeekCompleteListener(new AudioSeekCompleteListener());
			_player.setOnInfoListener(new AudioInfoListener());
			_player.setOnErrorListener(new AudioErrorListener());

			try {					
				// IDLE STATE

				// Set Player data Source
				if(session.getAudioFilePath().contains("http://") || session.getAudioFilePath().contains("https://"))
					_player.setDataSource(_ctx, Uri.parse(session.getAudioFilePath()));
				else
					_player.setDataSource(session.getAudioFilePath());

				// INITIALIZED STATE
				STATE = INITIALIZED;

				// Prepare the Media Player
				_player.prepareAsync();			

				// PREPARED STATE
				STATE = PREPARED;

			} catch (IllegalArgumentException e) {
				release();
				e.printStackTrace();
			} catch (SecurityException e) {
				release();
				e.printStackTrace();
			} catch (IllegalStateException e) {
				release();
				e.printStackTrace();
			} catch (IOException e) {
				release();
				e.printStackTrace();
			}
		}
	}



	/**
	 * Pause the playback if the media player is currently playing the
	 * audio session.
	 */
	public void pause(){
		if(null != _player){
			if(STATE == STARTED || STATE == PAUSED){
				_player.pause();
				STATE = PAUSED;
			}
		}
	}

	/**
	 * Stop the current playback session if the media player is in the correct state
	 * then set's the current state to 'STOPPED'
	 */
	public void stop(){
		if(null != _player){
			if(STATE == PREPARED || STATE == STARTED || STATE == STOPPED || STATE == PAUSED || STATE == COMPLETED){
				_player.stop();
				STATE = STOPPED;
			}
		}
	}

	/**
	 * Resume playback of the current playback session, this throws the state
	 * into 'STARTED' if invoked properly
	 */
	public void play(){
		if(null != _player){
			if(STATE == PREPARED || STATE == STARTED || STATE == PAUSED || STATE == COMPLETED){
				_player.start();
				STATE = STARTED;
			}
		}
	}

	/**
	 * Seek the media player to a specific point in time on
	 * the audio track
	 * @param milliseconds		the point in the audio track you wish to start playing from
	 */
	public void seekTo(int milliseconds){
		if(null != _player){
			if(STATE == PREPARED || STATE == STARTED || STATE == PAUSED || STATE == COMPLETED){
				_player.seekTo(milliseconds);
			}
		}
	}

	/**
	 * Destroy the media player, this relinquishes media player resources from 
	 * the player object and then nullifies itself. This will put the Player in the 'END' state which means
	 * it becomes useless until the user calls 'prepareAudioSession(...)' to prepare the session
	 */
	public void release(){
		if(null != _player){
			_player.release();
			_player = null;				// nullify the player
			_currentSession = null;		// nullify the current session
			STATE = END;
		}
	}

	/**************************************************************************************************************
	 * 
	 * 	MEDIA PLAYER SESSION INFO METHODS.
	 *  - methods used to gain information about the current audio session
	 * 
	 */

	/**
	 * Get the current audio session for this player
	 * @return
	 */
	public AudioSession getCurrentSession(){
		return _currentSession;
	}

	/**
	 * Get the current state of the audio player
	 * @return		the state constant { IDLE, INITIALIZED, PREPARED, STARTED, PAUSED, STOPPED, COMPLETED, END, ERROR }
	 */
	public int getCurrentState(){ return STATE; }
	public void setCurrentState(int val){ STATE = val;}

	/**
	 * Get the current time position of hte track in milliseconds 
	 * @return	the current time position, or -1 if this is not possible in the current state
	 */
	public int getCurrentPosition(){
		if(null != _player){
			if(STATE == IDLE || 
					STATE == INITIALIZED || 
					STATE == PREPARED || 
					STATE == STARTED || 
					STATE == PAUSED || 
					STATE == STOPPED || 
					STATE == COMPLETED){

				return _player.getCurrentPosition();				
			}
		}
		return -1;
	}

	/**
	 * Get the total duration of the current audio session
	 * @return	the total duration in milliseconds, or -1 if session is not properly prepared
	 */
	public int getTotalDuration(){
		if(null != _player){
			if(STATE == PREPARED || 
					STATE == STARTED || 
					STATE == PAUSED || 
					STATE == STOPPED || 
					STATE == COMPLETED){
				// RETURN THE DURATION
				return _player.getDuration();				
			}
		}
		return -1;
	}

	/**
	 * Return whether or not the media player is 
	 * currently playing or not
	 * 
	 * @return		true if playing, false if not, or player is null
	 */
	public boolean isPlaying(){
		if(null != _player){
			if(STATE != ERROR){
				return _player.isPlaying();
			}
		}
		return false;
	}
	
	/**
	 * Return whether the player is paused or not
	 * @return
	 */
	public boolean isPaused(){
		if(null != _player){
			if(STATE == PAUSED) return true;
		}
		return false;
	}

	/**
	 * Set teh Volume of the player
	 * 
	 * @param left		the left speaker volume
	 * @param right		the right speaker volume
	 */
	public void setVolume(float left, float right){
		if(null != _player){
			if(STATE != ERROR){
				_player.setVolume(left, right);
			}
		}
	}
	

	/**********************************************************
	 * Inner Classes & Interfaces
	 */

	class AudioCompletionListener implements MediaPlayer.OnCompletionListener{
		@Override
		public void onCompletion(MediaPlayer mp) {
			Utils.log(TAG, "Media Player has reached Completion");
		}		
	}

	class AudioSeekCompleteListener implements MediaPlayer.OnSeekCompleteListener{
		@Override
		public void onSeekComplete(MediaPlayer mp) {
			Utils.log(TAG, "Media Player Seek Complete [" + mp.getCurrentPosition() + ", " + mp.getDuration() + "]");
		}		
	}

	class AudioInfoListener implements MediaPlayer.OnInfoListener{
		@Override
		public boolean onInfo(MediaPlayer mp, int what, int extra) {
			Utils.log(TAG, "Media Player Info[" + what + ", " + extra + "]");
			return false;
		}		
	}

	class AudioErrorListener implements MediaPlayer.OnErrorListener{
		@Override
		public boolean onError(MediaPlayer mp, int what, int extra) {
			Utils.log(TAG, "Media Player Error[" + what + ", " + extra + "]");
			STATE = ERROR;
			return false;
		}
	}



}
