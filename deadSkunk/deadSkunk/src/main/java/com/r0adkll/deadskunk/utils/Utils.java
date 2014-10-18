package com.r0adkll.deadskunk.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PointF;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.webkit.MimeTypeMap;

import java.net.URLConnection;
import java.sql.Date;
import java.text.DecimalFormat;
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

	/**
	 * Variables
	 */
	
	// Random generator for use in the application
	private static Random random = new Random();
    private static Random nanoRandom;


	/* 
	 * Float Format Constants 
	 */
	public static final String ONE_DIGIT = "%.1f";
	public static final String TWO_DIGIT = "%.2f";
	public static final String THREE_DIGIT = "%.3f";

	/**
	 * Get the Random Number Generator
	 * @return  the static random class
	 */
	public static Random getRandom(){ return random; }

    /**
     * Get a random object freshly seeded with
     * the systems nano time
     *
     * @return  nano seeded random
     */
    public static Random getNanoRandom(){
        if(nanoRandom == null) nanoRandom = new Random(System.nanoTime());
        return nanoRandom;
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
     * Format a double number to a specified number of decimal places
     *
     * @param input             the double input
     * @param decimalPlaces     the number of decimal places allowed
     * @return                  the formated string output
     */
    public static String formatDecimal(double input, int decimalPlaces){
        String formatString = "#.";
        for(int i=0; i<decimalPlaces; i++){
            formatString = formatString.concat("#");
        }
        DecimalFormat df = new DecimalFormat(formatString);
        return df.format(input);
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
     * Return whether or not the device is running Lollipop 5.0
     */
    public static boolean isLollipop(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP);
    }

    /**
     * Return whether or not the device is running KitKat 4.4
     * @return
     */
    public static boolean isKitKat(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT);
    }

    /**
     * Return whether not the device is running JellyBean 4.3 or greater (MR2)
     */
    public static boolean isJellyBeanMR2(){
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2);
    }

    /**
     * Check to see if this device is running JellyBean 4.2 or greater (MR1)
     */
    public static boolean isJellyBeanMR1() { return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1); }
	
	/**
	 * Check to see if this device is running
	 * Jelly Bean or not
	 */
	public static boolean isJellyBean(){
		return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN);
	}

	/**
	 * Check to see if this device is running
	 * ICS or greater
	 */
	public static boolean isICS(){
		return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH);
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
     * Convert Density-Independent Pixels to actual pixels
     *
     * @param ctx       the application context
     * @param dpSize    the size in DP units
     * @return          the size in Pixel units
     */
    public static float dpToPx(Context ctx, float dpSize) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpSize, ctx.getResources().getDisplayMetrics());
    }

    /**
     * Convert Scale-Dependent Pixels to actual pixels
     *
     * @param ctx       the application context
     * @param spSize    the size in SP units
     * @return          the size in Pixel units
     */
    public static float spToPx(Context ctx, float spSize){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spSize, ctx.getResources().getDisplayMetrics());
    }

    /**
     * Clamp Integer values to a given range
     *
     * @param value     the value to clamp
     * @param min       the minimum value
     * @param max       the maximum value
     * @return          the clamped value
     */
    public static int clamp(int value, int min, int max){
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp Float values to a given range
     *
     * @param value     the value to clamp
     * @param min       the minimum value
     * @param max       the maximum value
     * @return          the clamped value
     */
    public static float clamp(float value, float min, float max){
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp Long values to a given range
     *
     * @param value     the value to clamp
     * @param min       the minimum value
     * @param max       the maximum value
     * @return          the clamped value
     */
    public static long clamp(long value, long min, long max){
        return Math.max(min, Math.min(max, value));
    }

    /**
     * Clamp Double values to a given range
     *
     * @param value     the value to clamp
     * @param min       the minimum value
     * @param max       the maximum value
     * @return          the clamped value
     */
    public static double clamp(double value, double min, double max){
        return Math.max(min, Math.min(max, value));
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * Get a thumbnail bitmap for a given video
     *
     * @param videoPath     the path to the video
     * @return              the thumbnail of the video, or null
     */
    public static Bitmap getVideoThumbnail(String videoPath){
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(videoPath);
        return mmr.getFrameAtTime();
    }

    /**
     * Blur an image
     *
     * @param src       the bitmap to blur
     * @param radius    the pixel radius to blur at
     * @return          the blurred bitmap
     */
    public static Bitmap blurImage(Context ctx, Bitmap src, float radius){
        // Load a clean bitmap and work from that.
        Bitmap blurredBmp = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);

        RenderScript rs = RenderScript.create(ctx);

        Allocation input = Allocation.createFromBitmap(rs, src);
        Allocation output = Allocation.createTyped(rs, input.getType());

        ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        script.setInput(input);

        script.setRadius(radius);
        script.forEach(output);

        output.copyTo(blurredBmp);

        return blurredBmp;
    }

}
