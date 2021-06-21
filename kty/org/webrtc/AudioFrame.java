package org.webrtc;

import java.nio.ByteBuffer;

public class AudioFrame {
    private ByteBuffer data;
    private int samples;
    private int sampleRate;
    private int channel;
    private long bitsPerSample;

    @CalledByNative
    AudioFrame(ByteBuffer data, int bitsPerSample, int sampleRate, int channel, int samples) {
        this.data = data;
        this.samples = samples;
        this.sampleRate = sampleRate;
        this.channel = channel;
        this.bitsPerSample = bitsPerSample;
    }

    public ByteBuffer getData() {
        return data;
    }

    public int getSamples() {
        return samples;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public int getChannel() {
        return channel;
    }

    public long getBitsPerSample() {
        return bitsPerSample;
    }
}
