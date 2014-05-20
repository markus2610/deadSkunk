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

package com.r0adkll.deadskunk.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.net.Uri;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;

import java.net.URLConnection;
import java.sql.Date;
import java.util.Calendar;
import java.util.IllegalFormatException;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * This is the standard utils file containing common util functions
 * throughout my applications.
 * 
 * @author drew.heavner
 *
 */
public class Utils {
	
	/**
	 * Constants
	 */

	/*
	 * This is the 'DEBUG' flag that signifies whether this application
	 * is in development mode, or production mode
	 */
	public static boolean DEBUG = false;

	/**
	 * Variables
	 */
	
	// Random generator for use in the application
	private static Random random = new Random();
	
	/* 
	 * Float Format Constants 
	 */
	public static final String ONE_DIGIT = "%.1f";
	public static final String TWO_DIGIT = "%.2f";
	public static final String THREE_DIGIT = "%.3f";

	/**
	 * Turn on debugging
	 * @param value
	 */
	public static void setDebug(boolean value){
		DEBUG = value;
	}

	/**
	 * Get the Random Number Generator
	 * @return
	 */
	public static Random getRandom(){ return random; }

    /**
     * Get a random object freshly seeded with
     * the systems nano time
     *
     * @return  nano seeded random
     */
    public static Random getNanoRandom(){
        return new Random(System.nanoTime());
    }
	
	/**
	 * Format a time in milliseconds to 'hh:mm:ss' format
	 * @param millis		the milliseconds time
	 * @return				the time in 'hh:mm:ss' format
	 */
	public static String formatTimeText(int millis){
		// Format the milliseconds into the form - 'hh:mm:ss'
		return String.format(Locale.US, "%02d:%02d:%02d", 
		    TimeUnit.MILLISECONDS.toHours(millis),
		    TimeUnit.MILLISECONDS.toMinutes(millis) - 
		    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		    TimeUnit.MILLISECONDS.toSeconds(millis) - 
		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}
	
	/**
	 * Format a time in milliseconds to 'mm:ss' format
	 * @param millis		the milliseconds time
	 * @return				the time in 'mm:ss' format
	 */
	public static String formatTimeTextMS(long millis){
		// Format the milliseconds into the form - 'hh:mm:ss'
		return String.format(Locale.US, "%02d:%02d",
		    TimeUnit.MILLISECONDS.toMinutes(millis) - 
		    TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
		    TimeUnit.MILLISECONDS.toSeconds(millis) - 
		    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
	}

    /**
     * Return whether or not this device is a tablet or a phone (2)
     * This one is slightly more efficient than the first one
     * by using the same comparison as the resource buckets for determining
     * which layouts/resources to grab
     *
     * @param ctx       the application context
     * @return          true if tablet, false if phone
     */
    public static boolean isTablet(Context ctx){
        if(ctx == null) return false;

        // Check to see if it is a large display
        boolean isLargeDisplay = (ctx.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK)
                >= Configuration.SCREENLAYOUT_SIZE_LARGE;

        if(isLargeDisplay){
            // get Display Metrics
            DisplayMetrics metrics = ctx.getResources().getDisplayMetrics();

            // Compute widthDP
            float widthDP = metrics.widthPixels / metrics.density;

            // If widthDp is > 600dp, then the device is a tablet
            if(widthDP >= 600){
                return true;
            }
        }

        return false;
    }

    /**
     * Return a user readable string of the time elapsed since the date
     * provided and the current time. i.e. 2s, 4h 10m, 5days
     * @param date      the comparison date
     * @return          the user readable string
     */
    public static String printTimeDuration(java.util.Date date){
        if(date == null) return "";

        // get the different in milliseconds
        long targetDateMS = date.getTime();
        long currentDateMS = System.currentTimeMillis();
        long timeDiffMS = Math.abs(currentDateMS - targetDateMS);

        long days = TimeUnit.MILLISECONDS.toDays(timeDiffMS);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiffMS);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiffMS);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiffMS);

        if(days > 0){

            String suffix = days == 1 ? "day" : "days";
            String time = String.valueOf(days) + suffix;
            return time;

        }else if(hours > 0){

            long min = (minutes - (hours * 60));
            String timeMin = " " + String.valueOf(min) + "m";
            String time = String.valueOf(hours) + "h" + ((min > 0) ? timeMin : "");
            return time;

        }else if(minutes > 0){

            String time = String.valueOf(minutes) + "m";
            return time;

        }else if(seconds > 0){

            String time = String.valueOf(seconds) + "s";
            return time;

        }

        return "a while";
    }

    /**
     * Print the time duration of the date given from the current time
     *
     * @param date      the comparison date
     * @return          the user readable string
     */
    public static String printTimeDurationLong(java.util.Date date){
        if(date == null) return "";

        // get the different in milliseconds
        long targetDateMS = date.getTime();
        long currentDateMS = System.currentTimeMillis();
        long timeDiffMS = Math.abs(currentDateMS - targetDateMS);

        long days = TimeUnit.MILLISECONDS.toDays(timeDiffMS);
        long hours = TimeUnit.MILLISECONDS.toHours(timeDiffMS);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(timeDiffMS);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(timeDiffMS);

        if(days > 0){

            String suffix = days == 1 ? " day" : " days";
            String time = String.valueOf(days) + suffix;
            return time;

        }else if(hours > 0){

            long min = (minutes - (hours * 60));
            String timeMin = " " + String.valueOf(min) + "m";
            String time = String.valueOf(hours) + "h" + ((min > 0) ? timeMin : "");
            return time;

        }else if(minutes > 0){

            String time = String.valueOf(minutes) + " minutes";
            return time;

        }else if(seconds > 0){

            String time = String.valueOf(seconds) + " seconds";
            return time;

        }

        return "a while";
    }

    /**
     * Check for a running service
     *
     * @param ctx                   the application context
     * @param serviceClassName      the service class name
     * @return      true if service is running, false overwise
     */
    public static boolean checkForRunningService(Context ctx, String serviceClassName) {
        ActivityManager manager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClassName.equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
	
	/**
	 * Get the current Epoch Time
	 * @return		current time in epoch
	 */
	public static long getEpochTime(){
		return System.currentTimeMillis()/1000;
	}
	
	/**
	 * Convert Epoch time to a Date object
	 * @param epoch
	 * @return
	 */
	public static Date getDateFromEpoch(long epoch){
		return new Date(epoch*1000);
	}
	
	/**
	 * Get the Device's GMT Offset
	 * @return	the gmt offset in hours
	 */
	public static int getGMTOffset(){
		Calendar now = Calendar.getInstance();
		return (now.get(Calendar.ZONE_OFFSET) + now.get(Calendar.DST_OFFSET))  / 3600000;
	}

    /**
     * Check to see if this device is running
     * ICS or greater
     */
    public static boolean isICS(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
    }
	
	/**
	 * Check to see if this device is running
	 * Jelly Bean 4.1 or greater
	 */
	public static boolean isJellyBean(){
		return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
	}

    /**
     * Return whether or not this device is running
     * Jellybean 4.2 or greater
     */
    public static boolean isJellyBeanMR1() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1);
    }

    /**
     * Return whether or not this device is running
     * Jellybean 4.3 or greater
     */
    public static boolean isJellyBeanMR2() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
    }

    /**
     * Return whether or not this device is running
     * KitKat (4.4) or greater
     */
    public static boolean isKitKat(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
    }
	
	/**
	 * Get the MIME type of a file
	 * @param url
	 * @return
	 */
	public static String getMimeType(String url)
	{
	    String type = null;
	    String extension = MimeTypeMap.getFileExtensionFromUrl(url);
	    if (extension != null) {
	        MimeTypeMap mime = MimeTypeMap.getSingleton();
	        type = mime.getMimeTypeFromExtension(extension);
	    }
	    return type;
	}

    public static String getMimeTypeFromExt(String ext){
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        if(mime != null)
            return mime;
        else
            return "";
    }
	
	/**
	 * Parse a file's mime type from the file extension
	 * 
	 * @param filename
	 * @return
	 */
	public static String parseMimeType(String filename){
		return URLConnection.guessContentTypeFromName(filename);
	}
	
	/**
	 * Condense a file size in bytes into a more proper form
	 * of kilobytes, megabytes, gigabytes
	 * @param bytes		the size in bytes
	 * @return			the condensed string
	 */
	public static String condenseFileSize(long bytes){
		
		// Kilobyte Check
		long kilo = bytes / 1024L;
		long mega = kilo / 1024L;
		long giga = mega / 1024L;
		
		// Determine which value to send back
		if(giga > 1) 
			return giga + " Gb";
		else if(mega > 1)
			return mega + " Mb";
		else if(kilo > 1)
			return kilo + " Kb";
		else 
			return bytes + " bytes";
	}
	
	/**
	 * Condense a file size in bytes into a more proper form
	 * of kilobytes, megabytes, gigabytes
	 * @param bytes		the size in bytes
     * @param precision     the precision constant {@code ONE_DIGIT}, {@code TWO_DIGIT}, {@code THREE_DIGIT}
	 * @return			the condensed string
	 */
	public static String condenseFileSize(long bytes, String precision) throws IllegalFormatException{
		
		// Kilobyte Check
		float kilo = bytes / 1024f;
		float mega = kilo / 1024f;
		float giga = mega / 1024f;
		
		// Determine which value to send back
		if(giga > 1) 
			return String.format(precision + " GB", giga);
		else if(mega > 1)
			return String.format(precision + " MB", mega);
		else if(kilo > 1)
			return String.format(precision + " KB", kilo);
		else 
			return bytes + " b";
	}


	
	/**
	 * Start the email with pre-filled information
	 */
	public static void sendSupportEmail(Context ctx, String addr, String subject){
		
		// Create Email Intent
		Intent email = new Intent(Intent.ACTION_SEND);
		email.setType("plain/text");
		email.putExtra(Intent.EXTRA_EMAIL, new String[] { addr });
		email.putExtra(Intent.EXTRA_SUBJECT, subject);
		
		// Start Intent
		ctx.startActivity(Intent.createChooser(email, "Send Support Email"));		
	}
	
	/**
	 * Launch the Play Store details of this app
	 * @param ctx
	 */
	public static void openAppDetails(Context ctx){
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse("market://details?id=" + ctx.getPackageName()));
		ctx.startActivity(i);
	}

    /**
     * Compute the distance between two points
     *
     * @param p1		the first point
     * @param p2		the second point
     * @return			the distance between the two points
     */
    public static float distance(PointF p1, PointF p2){
        return (float) Math.sqrt(Math.pow((p2.x - p1.x), 2) + Math.pow(p2.y - p1.y,2));
    }

    /**
     * Convert Density Independent Pixels to Pixels
     *
     * @param ctx   the application context
     * @param dp    the dp unit to convert
     * @return      the px amount for dp
     */
    public static float dpToPx(Context ctx, float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, ctx.getResources().getDisplayMetrics());
    }

	/**
	 * Easy method for Logging 'DEBUG' messages
	 * @param msg		the message
	 * @param tag		the identifing tag
	 */
	public static void log(String tag, String msg){
		log(Log.DEBUG, tag, msg);
	}

	/**
	 * Easy method for Logging 'INFO' messages
	 * @param tag		the message
	 * @param msg		the identifing tag
	 */
	public static void logi(String tag, String msg){
		log(Log.INFO, tag, msg);
	}

	/**
	 * Easy method for Logging 'ERROR' messages
	 * @param tag		the message
	 * @param msg		the identifing tag
	 */
	public static void loge(String tag, String msg){
		log(Log.ERROR, tag, msg);
	}

	/**
	 * Easy method for Logging 'WARN' messages
	 * @param tag		the message
	 * @param msg		the identifing tag
	 */
	public static void logw(String tag, String msg){
		log(Log.WARN, tag, msg);
	}

    /**
     * Easy method for Loggin 'WTF' messages
     * @param tag
     * @param msg
     */
    public static void logwtf(String tag, String msg){
        if(DEBUG)
            Log.wtf(tag, msg);
    }

	/**
	 * Easy Wrapper to printing to android Log
	 * @param priority		the message priority
	 * @param msg			the message
	 * @param tag			the identifing tag
	 */
	public static void log(int priority, String tag, String msg){
		if(DEBUG)
			Log.println(priority, tag, msg);
	}

}
