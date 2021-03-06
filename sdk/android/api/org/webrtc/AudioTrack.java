/*
 *  Copyright 2013 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

package org.webrtc;

import java.util.IdentityHashMap;

/** Java wrapper for a C++ AudioTrackInterface */
public class AudioTrack extends MediaStreamTrack {
  private final IdentityHashMap<AudioSink, Long> sinks = new IdentityHashMap<>();
  public AudioTrack(long nativeTrack) {
    super(nativeTrack);
  }

  /** Sets the volume for the underlying MediaSource. Volume is a gain value in the range
   *  0 to 10.
   */
  public void setVolume(double volume) {
    nativeSetVolume(getNativeAudioTrack(), volume);
  }

  /** Returns a pointer to webrtc::AudioTrackInterface. */
  long getNativeAudioTrack() {
    return getNativeMediaStreamTrack();
  }

  public void addSink(AudioSink sink) {
    if (sink == null) {
      throw new IllegalArgumentException("The VideoSink is not allowed to be null");
    }
    // We allow calling addSink() with the same sink multiple times. This is similar to the C++
    // VideoTrack::AddOrUpdateSink().
    if (!sinks.containsKey(sink)) {
      final long nativeSink = nativeWrapSink(sink);
      sinks.put(sink, nativeSink);
      nativeAddSink(getNativeMediaStreamTrack(), nativeSink);
    }
  }

  /**
   * Removes a VideoSink from the track.
   *
   * If the VideoSink was not attached to the track, this is a no-op.
   */
  public void removeSink(AudioSink sink) {
    final Long nativeSink = sinks.remove(sink);
    if (nativeSink != null) {
      nativeRemoveSink(getNativeMediaStreamTrack(), nativeSink);
      nativeFreeSink(nativeSink);
    }
  }

  @Override
  public void dispose() {
    for (long nativeSink : sinks.values()) {
      nativeRemoveSink(getNativeMediaStreamTrack(), nativeSink);
      nativeFreeSink(nativeSink);
    }
    sinks.clear();
    super.dispose();
  }

  private static native void nativeSetVolume(long track, double volume);

  private static native long nativeWrapSink(AudioSink sink);

  private static native void nativeFreeSink(long sink);

  private static native void nativeAddSink(long track, long sink);

  private static native void nativeRemoveSink(long track, long sink);
}
