package com.r0adkll.deadskunk.fragments;

import com.r0adkll.deadskunk.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

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
