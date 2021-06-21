package org.webrtc;

/**
 * Java version of rtc::AudioSinkInterface.
 */
public interface AudioSink {
    /**
     * when call complete, frame.data may be released
     * @param frame
     */
    @CalledByNative void onData(AudioFrame frame);
}