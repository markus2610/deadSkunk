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

package com.r0adkll.deadskunk.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Optimized List adapter based on Android Effiecent List Adapter
 * sample (List14.java)
 * 
 * @author r0adkll
 *
 * @param <T>
 */
public abstract class BetterListAdapter<T> extends ArrayAdapter<T>{

	/**
	 * Variables
	 */
	private int viewResource = -1;
	private LayoutInflater inflater;
	
	/**
	 * Constructor
	 * @param context					application context
	 * @param textViewResourceId		the item view id
	 * @param objects					the list of objects
	 */
	public BetterListAdapter(Context context, int textViewResourceId,
			List<T> objects) {
		super(context, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		
		viewResource = textViewResourceId;
		
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * The data holder for the view
	 * 
	 * @author drew.heavner
	 */
	public static class ViewHolder{	}
	
	/**
	 * Called to retrieve the view 
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent){
		
		ViewHolder holder;
		
		if(convertView == null){
			
			// Load the view from scratch
			convertView = inflater.inflate(viewResource, parent, false);
			
			// Load the ViewHolder
			holder = createHolder(convertView);
			
			// set holder to the tag
			convertView.setTag(holder);
			
		}else{
			
			// Pull the view holder from convertView's tag
			holder = (ViewHolder) convertView.getTag();
			
		}	
		
		// bind the data to the holder
		bindHolder(holder, position);	
		
		return convertView;
	}
	
	
	
	/**
	 * Create View/View holder
	 * 
	 * 	Here you load all your views from the layout into a custom 
	 *  view holder that you overrid from ViewHolder();
	 * 
	 * @param view
	 * @return
	 */
	public abstract ViewHolder createHolder(View view);
	
	/**
	 * Bind the Data from the data object to the view elements you
	 * created in 'createHolder()'
	 * 
	 * @param holder
	 * @param position
	 */
	public abstract void bindHolder(ViewHolder holder, int position);
	
}
