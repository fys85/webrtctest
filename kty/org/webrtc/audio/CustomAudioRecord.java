package org.webrtc.audio;

import org.webrtc.CalledByNative;
import org.webrtc.Logging;

import java.nio.ByteBuffer;

public class CustomAudioRecord {
    private static final String TAG = CustomAudioRecord.class.getSimpleName();
    private boolean isStartRecording = false;
    private ByteBuffer cachedBuffer;
    private int sampleRate;
    private int channels;
    private long nativeAudioRecord;

    public CustomAudioRecord(int sampleRate, int channels) {
        this.sampleRate = sampleRate;
        this.channels = channels;
    }

    @CalledByNative
    public boolean startRecording() {
        isStartRecording = true;
        return true;
    }

    @CalledByNative
    public boolean stopRecording() {
        isStartRecording = false;
        return true;
    }

    @CalledByNative
    boolean isAcousticEchoCancelerSupported() {
        return false;
    }

    @CalledByNative
    boolean isNoiseSuppressorSupported() {
        return false;
    }

    @CalledByNative
    private boolean enableBuiltInAEC(boolean enable) {
        return false;
    }

    @CalledByNative
    private boolean enableBuiltInNS(boolean enable) {
        return false;
    }

    /**
     * @param sampleRate
     * @param channels
     * @return 10ms
     */
    @CalledByNative
    private int initRecording(int sampleRate, int channels) {
        return sampleRate / 100;
    }

    @CalledByNative
    public void setNativeAudioRecord(long audioRecord) {
        nativeAudioRecord = audioRecord;
    }

    public void sendCustomAudioData(ByteBuffer data, int sampleRate, int channels) {
        if (this.sampleRate != sampleRate || this.channels != channels) {
            Logging.d(TAG, "malform audio frame format");
            return;
        }
        if (isStartRecording) {
            if (cachedBuffer == null) {
                cachedBuffer = ByteBuffer.allocateDirect(data.capacity());
                nativeCacheDirectBufferAddress(nativeAudioRecord, cachedBuffer);
            }
            cachedBuffer.put(data);
            cachedBuffer.rewind();
            nativeDataIsRecorded(nativeAudioRecord, cachedBuffer.remaining());
        }
    }

    private native void nativeCacheDirectBufferAddress(
            long nativeCustomAudioRecordJni, ByteBuffer byteBuffer);

    private native void nativeDataIsRecorded(long nativeCustomAudioRecordJni, int bytes);
}