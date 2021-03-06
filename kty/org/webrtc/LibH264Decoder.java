package org.webrtc;

public class LibH264Decoder extends WrappedNativeVideoDecoder {
    @Override
    public long createNativeVideoDecoder() {
        return nativeCreateDecoder();
    }

    static native long nativeCreateDecoder();

    static native boolean nativeIsSupported();
}
