package com.r0adkll.deadskunk.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.*;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

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
    private ProgressBar _loading;

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
        _loading = (ProgressBar) getActivity().findViewById(R.id.loading_bar);

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

    public void showLoading(){
        _loading.setVisibility(View.VISIBLE);
    }

    public void hideLoading(){
        _loading.setVisibility(View.INVISIBLE);
    }

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

        // Set the webview client
        _web.setWebViewClient(webClient);

        // Load the URL
		_web.loadUrl(_url);
        showLoading();
	}


    /**
     * The webview client
     */
    private WebViewClient webClient = new WebViewClient(){
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoading();
        }
    };

}
