package org.webrtc;

public class LibH264Encoder extends WrappedNativeVideoEncoder {
    private VideoCodecInfo mCodecInfo;

    public LibH264Encoder(VideoCodecInfo codecInfo) {
        mCodecInfo = codecInfo;
    }

    @Override
    public long createNativeVideoEncoder() {
        return nativeCreateEncoder(mCodecInfo);
    }

    static native long nativeCreateEncoder(VideoCodecInfo codecInfo);

    @Override
    public boolean isHardwareEncoder() {
        return false;
    }

    static native boolean nativeIsSupported();
}
