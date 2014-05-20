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
