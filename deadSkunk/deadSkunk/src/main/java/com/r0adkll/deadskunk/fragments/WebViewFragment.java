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

package com.r0adkll.deadskunk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebView;
import com.r0adkll.deadskunk.R;

/**
 * This fragment will be used to display a page in a webview
 * @author drew.heavner
 *
 */
public class WebViewFragment extends Fragment{

	/***********************************************************
	 * Static Initializer 
	 * 
	 */

	/**
	 * Create an instance of the WebView fragment that is set to the 
	 * passed url to display.
	 * 
	 * @param url	the url to display
	 * @return		the webview fragment
	 */
	public static WebViewFragment createInstance(String url, boolean zoomControls, boolean isFullScreen){
		WebViewFragment frag = new WebViewFragment();
		frag.setURL(url);
		frag.setHasZoomControls(zoomControls);
		frag.setFullScreenMode(isFullScreen);
		return frag;
	}


	/**
	 * Variables
	 */
	private WebView _web;
	private String _url;
	private boolean _zoomCtrls = false;
	private boolean _fullScreen = false;

	/***********************************************************
	 * Lifecycle Methods 
	 * 
	 */

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);

		// Load the Webview from layout
		_web = (WebView) getActivity().findViewById(R.id.webview);

		// Initialize the webview
		initWebView();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup root, Bundle savedInstanceState){
		return inflater.inflate(R.layout.fragment_webview, root, false);
	}
	
	/**
     * This is called to inflate the action bar menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	menu.clear();
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			getFragmentManager().popBackStack();
			return true;
		}
		return false;
	}


	/***********************************************************
	 * Helper Methods 
	 * 
	 */

	/**
	 * Set the webview's URL
	 * @param url		the url to display
	 */
	public void setURL(String url){
		_url = url;
	}

	public void setHasZoomControls(boolean val){
		_zoomCtrls = val;
	}
	
	public void setFullScreenMode(boolean val){
		_fullScreen = val;
	}

	/**
	 * Initialize the Web View
	 */
	private void initWebView(){
		_web.getSettings().setBuiltInZoomControls(_zoomCtrls);
		_web.getSettings().setLoadWithOverviewMode(_fullScreen);
		_web.getSettings().setUseWideViewPort(_fullScreen);
		_web.loadUrl(_url);		
	}

}
