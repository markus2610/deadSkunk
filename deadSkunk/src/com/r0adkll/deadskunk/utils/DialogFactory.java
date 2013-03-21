package com.r0adkll.deadskunk.utils;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.r0adkll.deadskunk.R;
import com.r0adkll.deadskunk.R.drawable;

/**
 * This pump-outs easily configured
 * Dialogs for Displaying content
 * 
 * @author r0adkll
 *
 */
public class DialogFactory {



	
	/**
	 * Get a HoloEverywhere progress dialog 
	 * for server loading
	 * @param ctx     Application Context
	 * @param message   the Message to show
	 * @return        the compiled dialog object (unshown)
	 */
	public static ProgressDialog createProgressDialog(Context ctx, String message){
		ProgressDialog dialog = new ProgressDialog(ctx);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setMessage(message);
		dialog.setCancelable(false);
		return dialog;
	}

	/**
	 * Create an Alert Dialog that displays information to a user
	 * and the only action is 'Ok' which closes it 
	 * 
	 * @param message   the server reponse message
	 */
	public static void createAlertDialog(Context ctx, String message, String title){

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title)
		.setIcon(R.drawable.ic_launcher)
		.setMessage(message)
		.setCancelable(false)             
		.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Creates an Alert Dialog that has 2 action options of 'Yes' and 'No' in the form of the 
	 * call back functions 'onConfirmed()' and 'onCanceled()'
	 * @param ctx     the application context to create the dialog with
	 * @param msg     the message of the alert dialog class
	 * @param title     the title of the alert dialog
	 * @param listener    the action listener for button clicks
	 */
	public static void createAlertDialog(Context ctx, String msg, String title, final IAlertListener listener){

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title)
		.setIcon(R.drawable.ic_launcher)
		.setMessage(msg)
		.setCancelable(false)    

		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(listener!=null) listener.onConfirmed();
				dialog.dismiss();
			}
		})

		.setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(listener!=null) listener.onCanceled();
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Creates an Alert Dialog that has 2 action options of 'Yes' and 'No' in the form of the 
	 * call back functions 'onConfirmed()' and 'onCanceled()'
	 * @param ctx     the application context to create the dialog with
	 * @param msg     the message of the alert dialog class
	 * @param title     the title of the alert dialog
	 * @param listener    the action listener for button clicks
	 */
	public static void createAlertDialog(Context ctx, String posBtn, String negBtn, String msg, String title, final IAlertListener listener){

		AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
		builder.setTitle(title)
		.setIcon(R.drawable.ic_launcher)
		.setMessage(msg)
		.setCancelable(false)    

		.setPositiveButton(posBtn, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(listener!=null) listener.onConfirmed();
				dialog.dismiss();
			}
		})

		.setNegativeButton(negBtn, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int id) {
				if(listener!=null) listener.onCanceled();
				dialog.cancel();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}


	/**
	 * Alert Dialog Listener
	 * @author r0adkll
	 *
	 */
	public static interface IAlertListener{
		public void onConfirmed();
		public void onCanceled();
	}














}
