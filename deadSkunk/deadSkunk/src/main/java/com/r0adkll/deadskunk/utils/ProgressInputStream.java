package com.r0adkll.deadskunk.utils;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This Intercepts and updateds a progress listener
 * on the progress of the I/O task
 *
 * Created by r0adkll on 5/19/13.
 */
public class ProgressInputStream extends FilterInputStream {
    private static final String TAG = "PROGRESS_INPUT_STREAM";

    /**
     * *********************************************************
     * Variables
     */
    private final long mInputLength;
    private volatile long mTotalBytesRead;
    private OnProgressListener mProgressListener;

    /**
     * Constructor
     *
     * @param in
     * @param maxNumBytes
     */
    public ProgressInputStream(InputStream in, long maxNumBytes, OnProgressListener handler) {
        super(in);
        mTotalBytesRead = 0;
        mInputLength = maxNumBytes;
        mProgressListener = handler;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        return updateProgress(super.read());
    }

    /**
     * Internal method to update the io progress
     *
     * @param numBytesRead
     * @return
     */
    private int updateProgress(final int numBytesRead) {
        if (numBytesRead > 0) {
            mTotalBytesRead += numBytesRead;
        }

        // Update the progress listener
        mProgressListener.onProgress(mTotalBytesRead, mInputLength);

        return numBytesRead;
    }

    /***********************************************************
     *
     * Inner Interfaces
     *
     */


    public static interface OnProgressListener {
        public void onProgress(long read, long total);
    }

}