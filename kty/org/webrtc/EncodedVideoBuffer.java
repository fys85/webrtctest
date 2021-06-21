package org.webrtc;

import android.support.annotation.Nullable;

import java.nio.ByteBuffer;

public class EncodedVideoBuffer implements VideoFrame.Buffer {
    private static final String TAG = EncodedVideoBuffer.class.getSimpleName();
    private ByteBuffer data;
    private final int width;
    private final int height;
    private boolean isKeyFrame;
    private final RefCountDelegate refCountDelegate;

    public EncodedVideoBuffer(ByteBuffer data, int width, int height, boolean isKeyFrame, @Nullable Runnable releaseCallback) {
        this.data = data;
        this.width = width;
        this.height = height;
        this.isKeyFrame = isKeyFrame;
        this.refCountDelegate = new RefCountDelegate(releaseCallback);
    }

    public ByteBuffer getData() {
        return data;
    }

    public boolean isKeyFrame() {
        return isKeyFrame;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public VideoFrame.I420Buffer toI420() {
        return null;
    }

    @Override
    public void retain() {
        refCountDelegate.retain();
    }

    @Override
    public void release() {
        refCountDelegate.release();
    }

    @Override
    public VideoFrame.Buffer cropAndScale(int cropX, int cropY, int cropWidth, int cropHeight, int scaleWidth, int scaleHeight) {
        return null;
    }

    @Override
    public boolean isEncodedFrameBuffer() {
        Logging.d(TAG, "isEncodedFrameBuffer");
        return true;
    }
}
