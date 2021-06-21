package org.webrtc;

import java.nio.ByteBuffer;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

public class EncodedVideoEncoder implements VideoEncoder, Runnable {
    private final static String TAG = EncodedVideoEncoder.class.getSimpleName();
    private final int POLL_FRAME_TIMEOUT = 10;
    private Callback mCallback;
    private boolean bEncodeStart = false;
    private VideoCodecType mCodecType;
    private final BlockingDeque<EncodedImage.Builder> outputBuilders = new LinkedBlockingDeque<>();
    private final BlockingDeque<EncodedVideoBuffer> outputBuffers = new LinkedBlockingDeque<>();


    public EncodedVideoEncoder(VideoCodecInfo videoCodecInfo) {
        mCodecType = VideoCodecType.valueOf(videoCodecInfo.name);
    }

    @Override
    public VideoCodecStatus initEncode(Settings settings, Callback encodeCallback) {
        Logging.d(TAG, "initEncode");
        mCallback = encodeCallback;
        bEncodeStart = true;
        new Thread(this).start();
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoCodecStatus release() {
        bEncodeStart = false;
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoCodecStatus encode(VideoFrame videoFrame, EncodeInfo info) {
        if (!(videoFrame.getBuffer() instanceof EncodedVideoBuffer))
            return VideoCodecStatus.ERROR;
        EncodedImage.Builder builder = EncodedImage.builder()
                .setCaptureTimeNs(videoFrame.getTimestampNs())
                .setCompleteFrame(true)
                .setEncodedWidth(videoFrame.getBuffer().getWidth())
                .setEncodedHeight(videoFrame.getBuffer().getHeight())
                .setRotation(videoFrame.getRotation());
        outputBuffers.offer((EncodedVideoBuffer) (videoFrame.getBuffer()));
        outputBuilders.offer(builder);
//        Logging.d(TAG, "request encode one frame, is key frame: " + ((EncodedVideoBuffer) videoFrame.getBuffer()).isKeyFrame());
        return VideoCodecStatus.OK;
    }

    @Override
    public VideoCodecStatus setRateAllocation(BitrateAllocation allocation, int framerate) {
        return VideoCodecStatus.OK;
    }

    @Override
    public ScalingSettings getScalingSettings() {
        return new ScalingSettings(false);
    }

    @Override
    public String getImplementationName() {
        return "KtEncodedVideoEncoder";
    }

    @Override
    public void run() {
        while (bEncodeStart) {
            deliverEncodedImage();
        }
    }

    private void deliverEncodedImage() {
        try {
            EncodedImage.Builder builder = outputBuilders.poll(POLL_FRAME_TIMEOUT, TimeUnit.MILLISECONDS);
            if (builder == null) {
                return;
            }

            EncodedVideoBuffer encodedVideoFrame = outputBuffers.poll(POLL_FRAME_TIMEOUT, TimeUnit.MILLISECONDS);

            boolean isKeyFrame = encodedVideoFrame.isKeyFrame();
            ByteBuffer frameBuffer = encodedVideoFrame.getData().slice();
            final EncodedImage.FrameType frameType = isKeyFrame
                    ? EncodedImage.FrameType.VideoFrameKey
                    : EncodedImage.FrameType.VideoFrameDelta;

            builder.setBuffer(frameBuffer).setFrameType(frameType);
            mCallback.onEncodedFrame(builder.createEncodedImage(), new CodecSpecificInfo());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
