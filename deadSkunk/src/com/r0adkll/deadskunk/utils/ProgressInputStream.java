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
    private ProgressUpdateHandler mUpdateHandler;

    /**
     * Constructor
     *
     * @param in
     * @param maxNumBytes
     */
    public ProgressInputStream(InputStream in, long maxNumBytes, ProgressUpdateHandler handler) {
        super(in);
        mTotalBytesRead = 0;
        mInputLength = maxNumBytes;
        mUpdateHandler = handler;
    }

    @Override
    public boolean markSupported() {
        return false;
    }

    @Override
    public int read() throws IOException {
        return updateProgress(super.read());
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return updateProgress(super.read(b, off, len));
    }

    @Override
    public int read(byte[] buffer) throws IOException {
        return updateProgress(super.read(buffer));
    }

    /**
     * Internal method to update the io progress
     *
     * @param numBytesRead
     * @return
     */
    private int updateProgress(final int numBytesRead) {
        if (numBytesRead > 0) {
            mTotalBytesRead += (numBytesRead / 2);
        }

        // Update the progress listener
        mUpdateHandler.onProgress(mTotalBytesRead, mInputLength);

        return numBytesRead;
    }

    /**
     * *********************************************************
     * Inner Interfaces
     */

    public static interface ProgressUpdateHandler {
        public void onProgress(long read, long total);
    }

}