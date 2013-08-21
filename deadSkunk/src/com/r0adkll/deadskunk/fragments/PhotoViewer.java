package com.r0adkll.deadskunk.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.r0adkll.deadskunk.R;
import com.r0adkll.deadskunk.utils.Utils;
import com.r0adkll.deadskunk.views.PhotoView.PhotoViewAttacher;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * This dialog fragment creates a viewer dialog (fullscreen + transparent) for viewing images
 * from your cloud stores.
 *
 * Created by drew.heavner on 7/11/13.
 */
public class PhotoViewer extends DialogFragment{
    public static final String TAG = "PHOTO_VIEWER";

    /*****************************************************************
     * Static Initializers
     *
     */

    /**
     * Create an instance of the photo viewer with a set drawable
     * @param image     the image to view
     * @return          the new instance of the PhotoViewer dialog fragment
     */
    public static PhotoViewer createInstance(Drawable image){
        PhotoViewer pv = new PhotoViewer();
        pv.setDrawable(image);
        return pv;
    }

    /**
     * Create an instance of the photo viewer with an Image URL to be downloaded
     * with Picasso then set into the dialog
     *
     * @param imageUrl  the image url ot display
     * @return          the new instance of the PhotoViewer dialog fragment
     */
    public static PhotoViewer createInstance(String imageUrl){
        PhotoViewer pv = new PhotoViewer();
        pv.setDrawableURL(imageUrl);
        return pv;
    }

    /**
     * Create an instance of the photo viewer with an Image File reference to be downloaded
     * with Picasso then set into the dialog
     *
     * @param imageFile     the file of the image to preview
     * @return              the new instance of hte PhotoViewer dialog fragment
     */
    public static PhotoViewer createInstance(File imageFile){
        PhotoViewer pv = new PhotoViewer();
        pv.setDrawableFile(imageFile);
        return pv;
    }

    /*****************************************************************
     * Variables
     *
     *
     */

    // Photo View
    private ImageView mPhotoView;

    // Photo Viewer Controls - by Chris Banes - PhotoView
    private PhotoViewAttacher mAttacher;
    private Drawable mImage;
    private String mImageUrl;
    private File mImageFile;

    /**
     * Empty Constructor
     */
    public PhotoViewer(){}

    /*****************************************************************
     * Life Cycle Methods
     *
     *
     */


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Black_NoTitleBar);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(mImage != null){

            // Set Image Content, then
            mPhotoView.setImageDrawable(mImage);

            // Attach attacher
            mAttacher = new PhotoViewAttacher(mPhotoView);

        }else{

            if(null != mImageUrl && !mImageUrl.isEmpty()){

                // Download image with Picasso, and then update the photo view controls after it has been completed
                Picasso.with(getActivity())
                       .load(Uri.parse(mImageUrl))
                       .into(new Target() {
                           @Override
                           public void onSuccess(Bitmap bitmap) {
                               mImage = new BitmapDrawable(getResources(), bitmap);
                               mPhotoView.setImageDrawable(mImage);
                               mAttacher = new PhotoViewAttacher(mPhotoView);
                           }

                           @Override
                           public void onError() {
                               Utils.log(TAG, "Error loading image into Photo Viewer with Picasso");
                           }
                       });

            }else if(mImageFile != null){

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                try {
                    Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(mImageFile), new Rect(), options);
                    int width = options.outWidth;
                    int height = options.outHeight;

                    int scrnWidth = getResources().getDisplayMetrics().widthPixels;
                    int scrnHeight = getResources().getDisplayMetrics().heightPixels;

                    int newWidth, newHeight;
                    if (width > scrnWidth) {
                        newWidth = scrnWidth;
                        float ratio = (float) scrnWidth / (float) width;
                        newHeight = (int) (height * ratio);

                        Utils.log(TAG, "[PHOTO VIEWER] Dimen(" + newWidth + ", " + newHeight + ")");
                    }else{
                        newWidth = width;
                        newHeight = height;
                    }

                    Picasso.with(getActivity())
                            .load(mImageFile)
                            .skipCache()
                            .resize(newWidth, newHeight)
                            .into(new Target() {
                                @Override
                                public void onSuccess(Bitmap bitmap) {
                                    mPhotoView.setImageBitmap(bitmap);
                                    mAttacher = new PhotoViewAttacher(mPhotoView);
                                }

                                @Override
                                public void onError() {

                                }
                            });

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }



            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_photoviewer, container, false);
        mPhotoView = (ImageView) layout.findViewById(R.id.photo_image_view);
        return layout;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog diag = super.onCreateDialog(savedInstanceState);

        // Set Background to be transparent
        ColorDrawable background = new ColorDrawable(Color.BLACK);
        background.setAlpha(200);
        diag.getWindow().setBackgroundDrawable(background);

        return diag;
    }

    /*****************************************************************
     * Helper Methods
     *
     *
     */

    /**
     * Set the drawable for this photo viewer
     * @param image     the drawable to view
     */
    public void setDrawable(Drawable image){
        this.mImage = image;
    }

    /**
     * Set the url of the photo to download
     * @param url       the url of hte image to preview
     */
    public void setDrawableURL(String url){
        this.mImageUrl = url;
    }

    /**
     * Set the file of the photo to be viewed
     * @param file      the file of the image to view
     */
    public void setDrawableFile(File file){
        this.mImageFile = file;
    }

}
