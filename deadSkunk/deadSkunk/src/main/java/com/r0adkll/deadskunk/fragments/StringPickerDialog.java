package com.r0adkll.deadskunk.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import com.r0adkll.deadskunk.R;
import com.r0adkll.deadskunk.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * String Picker Dialog
 * 
 * @author drew.heavner
 *
 */
public class StringPickerDialog extends DialogFragment{
	private static final String TAG = "STRING_PICKER_DIALOG";
	
	/***********************************************
	 * Static Initializer
	 * 
	 */
	
	/**
	 * Create an instance of the string picker dialog
	 * @return
	 */
	public static StringPickerDialog createInstance(String title, String[]... content){
		StringPickerDialog diag = new StringPickerDialog();
		List<String[]> items = new ArrayList<String[]>();
		for(String[] stuff: content){
			items.add(stuff);
		}
		
		diag.setItems(items);		
		diag.setPickerTitle(title);
		return diag;
	}
	
	
	
	/**
	 * Variables
	 */
	private List<NumberPicker> _pickers = new ArrayList<NumberPicker>();
	private List<String[]> _items = new ArrayList<String[]>();
	private String _title;
	private IStringPickerCallback _listener;
	
	/************************************************
	 * Lifecycle Methods
	 * 
	 */
	
	/**
	 * Initial Create Method
	 * @param savedInstanceState
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set Style
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Holo_Light);
        
	}
	
	/**
	 * Called when activity is finished being created
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		
		getDialog().setTitle(_title);
		
		
		
		
	}
	
	/**
	 * Called to inflate the fragment's view
	 */
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        LinearLayout v = (LinearLayout)inflater.inflate(R.layout.fragment_stringpicker_dialog, container, false);                
        // For each list of items, create a number picker
        for(String[] content: _items){
        	// Create a new picker
        	NumberPicker picker = new NumberPicker(getActivity());
        	picker.setMinValue(0);
        	picker.setMaxValue(content.length-1);
        	picker.setDisplayedValues(content);

        	// Add teh picker to the view 
        	v.addView(picker);

        	// add picker to list
        	_pickers.add(picker);			
        }
        
        return v;
    }
	
	
	@Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        return new AlertDialog.Builder(getActivity())
                .setTitle(_title)
                .setPositiveButton("Okay",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	// Compile Selection
                        	String[] content = new String[_pickers.size()];
                        	for(NumberPicker picker: _pickers){
                        		int index = picker.getValue();
                        		int contentIndex = _pickers.indexOf(picker);
                        		
                        		String sval = getItemAtPosition(index, contentIndex);
                        		content[contentIndex] = sval;
                        	}
                        	
                        	if(_listener != null) _listener.onSelection(content);
                        }
                    }
                )
                .setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                        	if(_listener != null) _listener.onCancel();                        	
                        }
                    }
                )
                .create();
    }
	
	
	/************************************************
	 * Helper Methods
	 * 
	 */
	
	/**
	 * Set the Dialog selection listener
	 * @param listener
	 */
	public void setDialogListener(IStringPickerCallback listener){
		_listener = listener;
	}
	
	/**
	 * Set the Displayed List Items
	 * @param items
	 */
	public void setItems(List<String[]> items){
		_items = items;
	}
	
	/**
	 * Set the Dialog Title
	 * @param title
	 */
	public void setPickerTitle(String title){
		_title = title;
	}
	
	/**
	 * Get the Item at the specified position
	 * @param position		the position of the item to get
	 * @return				the item at the position
	 */
	public String getItemAtPosition(int position, int list){
		String[] content = _items.get(list);
		if(content == null){
			return "INVALID_LIST";
		}
		
		if(content.length-1 < position){
			return "INVALID_POSITION";
		}
		
		return content[position];
		
	}
	
	public static interface IStringPickerCallback{
		public void onSelection(String... content);
		public void onCancel();
	}
	
}
