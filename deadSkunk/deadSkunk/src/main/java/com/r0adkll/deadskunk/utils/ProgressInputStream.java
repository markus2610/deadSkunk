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