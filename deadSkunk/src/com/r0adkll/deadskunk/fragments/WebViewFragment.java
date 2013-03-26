package com.r0adkll.deadskunk.fragments;

import com.r0adkll.deadskunk.R;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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
	private ProgressBar _loading;
	private String _url;
	private boolean _zoomCtrls = false;
	private boolean _fullScreen = false;

	private MenuItem _back, _forward;
	
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
		_loading = (ProgressBar) getActivity().findViewById(R.id.loadingBar);

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
    	inflater.inflate(R.menu.menu_webview, menu);
    	_back = menu.findItem(R.id.menu_back);
    	_forward = menu.findItem(R.id.menu_forward);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		int id = item.getItemId();
		if(id == android.R.id.home){
			getFragmentManager().popBackStack();
			return true;
		}else if(id == R.id.menu_back){
			if(_web.canGoBack())
				_web.goBack();			
			return true;
		}else if(id == R.id.menu_forward){
			if(_web.canGoForward())
				_web.goForward();
			return true;
		}else if(id == R.id.menu_refresh){
			_web.reload();
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
		_web.setWebViewClient(mClient);
		_web.loadUrl(_url);	
	}
	
	private void checkActionStatus(){
		_back.setEnabled(_web.canGoBack());
		_forward.setEnabled(_web.canGoForward());
	}
	
	/***********************************************************
	 * Inner Classes and Interfaces
	 * 
	 */
	
	/**
	 * Simple WebViewClient that merely monitors page load start and ends
	 */
	private WebViewClient mClient = new WebViewClient(){
		@Override
		public void onPageFinished(WebView view, String url){
			super.onPageFinished(view, url);
			
			checkActionStatus();
			
			_loading.setVisibility(View.INVISIBLE);
			_web.bringToFront();
			
		}
		
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon){
			super.onPageStarted(view, url, favicon);
			
			checkActionStatus();
			
			_loading.setVisibility(View.VISIBLE);
			_loading.bringToFront();
			
		}
	};

}
