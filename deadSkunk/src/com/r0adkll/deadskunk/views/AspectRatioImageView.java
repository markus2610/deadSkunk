package com.r0adkll.deadskunk.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * This was created based on a snippet from StackOverflow 
 * from user @user Michel-F. Portzert at the thread
 * http://stackoverflow.com/questions/12059328/android-imageview-fit-width
 * 
 * 
 * @author drew.heavner
 *
 */
public class AspectRatioImageView extends ImageView{

	/**
	 * Constructor
	 * @param context
	 */
	public AspectRatioImageView(Context context) {
		super(context);
	}
	
	public AspectRatioImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AspectRatioImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	/**
	 * Maintain Image Aspect Ratio no matter the size
	 * 
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		if(getDrawable() != null){
		    int width = MeasureSpec.getSize(widthMeasureSpec);
		    int height = Math.round(width * ((float)getDrawable().getIntrinsicHeight() / (float)getDrawable().getIntrinsicWidth()));
		    setMeasuredDimension(width, height);
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
