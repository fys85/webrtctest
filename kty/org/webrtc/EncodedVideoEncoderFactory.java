package org.webrtc;

import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class EncodedVideoEncoderFactory implements VideoEncoderFactory {

    @Nullable
    @Override
    public VideoEncoder createEncoder(VideoCodecInfo info) {
        return new EncodedVideoEncoder(info);
    }

    @Override
    public VideoCodecInfo[] getSupportedCodecs() {
        return supportedCodecs();
    }


    static VideoCodecInfo[] supportedCodecs() {
        List<VideoCodecInfo> codecs = new ArrayList<VideoCodecInfo>();

        codecs.add(new VideoCodecInfo("H264", H264Utils.getDefaultH264Params(false)));

        return codecs.toArray(new VideoCodecInfo[codecs.size()]);
    }
}
