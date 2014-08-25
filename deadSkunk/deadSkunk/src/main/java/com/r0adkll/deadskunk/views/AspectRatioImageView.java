package com.r0adkll.deadskunk.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ImageView;
import com.r0adkll.deadskunk.R;

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

    // Ratio Type Constants
    public static final int RATIO_WIDTH = 0;
    public static final int RATIO_HEIGHT = 1;

    // The Ratio Type
    private int mRatioType = 0;

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
        int dimHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 140, getResources().getDisplayMetrics());

		if(getDrawable() != null){
            if(mRatioType == RATIO_WIDTH) {
                int width = MeasureSpec.getSize(widthMeasureSpec);
                int height = Math.round(width * ((float) getDrawable().getIntrinsicHeight() / (float) getDrawable().getIntrinsicWidth()));
                setMeasuredDimension(width, height);
            }else if(mRatioType == RATIO_HEIGHT){
                int height = dimHeight;
                int width = Math.round(height * ((float) getDrawable().getIntrinsicWidth() / (float) getDrawable().getIntrinsicHeight()));
                setMeasuredDimension(width, height);
            }
		}else{
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		}
	}
}
