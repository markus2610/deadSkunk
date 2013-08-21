package com.r0adkll.deadskunk.fragments;

import android.animation.Animator;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.r0adkll.deadskunk.R;
import com.r0adkll.deadskunk.adapters.BetterListAdapter;
import com.r0adkll.deadskunk.utils.Utils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an easy to use class for displaying project dependancies in
 * your Android applications.
 *
 * Created by drew.heavner on 8/21/13.
 */
public class LicenseFragment extends Fragment {
    private static final String TAG = "LICENSE_FRAGMENT";

    /**************************************************************
     *
     * Static initializer
     *
     */

    /**
     * Create an instance of the License fragment
     * @param   configId    the configuration xml resource id
     *
     * @return      the newly created fragment
     */
    public static LicenseFragment createInstance(int configId){
        LicenseFragment frag = new LicenseFragment();
        frag.mConfigFile = configId;
        return frag;
    }

    /**
     * Create an instance of the License fragment
     * @param   configId            the configuration xml resource id
     * @param   actionListener      the library item actions listener
     *
     * @return      the newly created fragment
     */
    public static LicenseFragment createInstance(int configId, LibraryListAdapter.ILibraryActionListener actionListener){
        LicenseFragment frag = new LicenseFragment();
        frag.mConfigFile = configId;
        frag.mActionListener = actionListener;
        return frag;
    }


    /**************************************************************
     *
     * Variables
     *
     */

    public static final String LIBRARY_TAG = "Library";
    public static final String ATTR_NAME = "name";
    public static final String ATTR_AUTHOR = "author";
    public static final String ATTR_SOURCE = "source";
    public static final String ATTR_LICENSE = "license";

    // The List View
    private ListView mList;
    private LibraryListAdapter mAdapter;
    private LibraryListAdapter.ILibraryActionListener mActionListener;

    // Library array
    private List<Library> mLibraries = new ArrayList<Library>();

    // XML Config File
    private int mConfigFile;


    /**************************************************************
     *
     * Lifecycle methods
     *
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_license, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Load listview
        mList = (ListView) getActivity().findViewById(R.id.license_list);

        // Look for saved licences file
        if(savedInstanceState != null){
            int savedConfigId = savedInstanceState.getInt("config_file", -1);
            if(savedConfigId != -1){
                mConfigFile = savedConfigId;
            }
        }

        // Safety
        if(mConfigFile == 0){
            Utils.log(TAG, "No valid config file for the LicenceFragment");
            getFragmentManager().popBackStack();
            return;
        }

        // Parse Configuration
        mLibraries = parseConfigFile();

        // Create adapter
        mAdapter = new LibraryListAdapter(getActivity(), R.layout.layout_library_item, mLibraries);
        mList.setAdapter(mAdapter);

        // Set action listener if available
        if(mActionListener != null){
            mAdapter.setActionListener(mActionListener);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("config_file", mConfigFile);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                // Pop the back stack
                getFragmentManager().popBackStack();
                return true;
        }
        return false;
    }

    /**************************************************************
     *
     * Helper Methods
     *
     */

    /**
     * Parse the XML config file and load the third party library references
     * @return      a list of parsed Library objects
     */
    private List<Library> parseConfigFile(){
        List<Library> libs = new ArrayList<Library>();

        // Load licence information from XML configuration in root application
        try{
            XmlResourceParser parser = getActivity().getResources().getXml(mConfigFile);

            // Parse dat shit
            parser.next();
            int eventType = parser.getEventType();
            while(eventType != XmlPullParser.END_DOCUMENT){

                // Look for tags
                if(eventType == XmlPullParser.START_TAG && parser.getName().equalsIgnoreCase(LIBRARY_TAG)){

                    // get Attributes
                    String name = parser.getAttributeValue(null, ATTR_NAME);
                    String author = parser.getAttributeValue(null, ATTR_AUTHOR);
                    String source = parser.getAttributeValue(null, ATTR_SOURCE);
                    String license = parser.getAttributeValue(null, ATTR_LICENSE);

                    // Construct library object, and add to return list
                    Library lib = new Library(name, author, source, "", license);
                    libs.add(lib);

                    //break;  ? Do i need this??
                }
                eventType = parser.next();
            }

        }catch(Resources.NotFoundException e){
            Utils.log(TAG, "ERROR: Config file not found [" + e.getLocalizedMessage() + "]");
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return libs;
    }

    /**
     * Set the action listener for when a user selects the source or license link
     * buttons. This is to allow maximum flexibility so the user of this object can specify
     * what method they want to use to display those weblinks (external, or internally)
     *
     * @param listener      the action listener
     */
    public void setLibraryActionListener(LibraryListAdapter.ILibraryActionListener listener){
        mActionListener = listener;
        if(mAdapter != null){
            mAdapter.setActionListener(mActionListener);
        }
    }

    /**************************************************************
     *
     * Inner Classes and Interfaces
     *
     */

    /**
     * The list adapter for the license fragment
     */
    private static class LibraryListAdapter extends BetterListAdapter<Library>{

        /**
         * Variables
         */
        private ILibraryActionListener mActionListener;

        /**
         * Constructor
         *
         * @param context            application context
         * @param textViewResourceId the item view id
         * @param objects            the list of objects
         */
        public LibraryListAdapter(Context context, int textViewResourceId, List<Library> objects) {
            super(context, textViewResourceId, objects);
        }

        /**
         * Set the action listener for this adapter
         * @param listener      the action listener
         */
        public void setActionListener(ILibraryActionListener listener){
            mActionListener = listener;
        }


        @Override
        public ViewHolder createHolder(View view) {
            LibraryViewHolder holder = new LibraryViewHolder();
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.author = (TextView) view.findViewById(R.id.author);
            holder.source = (ImageView) view.findViewById(R.id.source_link);
            holder.license = (ImageView) view.findViewById(R.id.license_link);
            return holder;
        }

        @Override
        public void bindHolder(ViewHolder holder, int position) {
            LibraryViewHolder lvh = (LibraryViewHolder) holder;
            final Library data = getItem(position);

            // Bind data to the view holder
            lvh.name.setText(data.name);
            lvh.author.setText(data.author);

            // Bind action listeners to image views
            lvh.source.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mActionListener != null) mActionListener.onSourceClicked(data.link);
                }
            });

            lvh.license.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(mActionListener != null) mActionListener.onLicenseClicked(data.licenseLink);
                }
            });

        }

        /**
         * This adapters viewholder
         */
        static class LibraryViewHolder extends ViewHolder{
            TextView name, author;
            ImageView source, license;
        }

        /**
         * This action listener for the source and licences buttons
         * in this adapter
         */
        public static interface ILibraryActionListener{
            public void onSourceClicked(String sourceLink);
            public void onLicenseClicked(String licenseLink);
        }

    }


    /**
     * Third party library representation class.
     * This contains all the information for specific library
     */
    public static class Library {
        /**
         * Variables
         */

        public String name;
        public String author;
        public String link;
        public String licenseName;
        public String licenseLink;

        /**
         * Empty Constructor
         */
        public Library(){}

        /**
         * String res constructor
         *
         * @param ctx               application context
         * @param nameRes           library name res id
         * @param authorRes         author name res id
         * @param linkRes           library link res id
         * @param licenseNameRes    library license name res id
         * @param licenseLinkRes    library license link res id
         */
        public Library(Context ctx, int nameRes, int authorRes, int linkRes, int licenseNameRes, int licenseLinkRes){
            name = ctx.getString(nameRes);
            author = ctx.getString(authorRes);
            link = ctx.getString(linkRes);
            licenseName = ctx.getString(licenseNameRes);
            licenseLink = ctx.getString(licenseLinkRes);
        }

        /**
         * Plain string constructor
         *
         * @param name
         * @param author
         * @param link
         * @param licenseName
         * @param licenseLink
         */
        public Library(String name, String author, String link, String licenseName, String licenseLink){
            this.name = name;
            this.author = author;
            this.link = link;
            this.licenseName = licenseName;
            this.licenseLink = licenseLink;
        }

    }

}
