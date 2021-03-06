/*
 *  Copyright 2017 The WebRTC project authors. All Rights Reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS.  All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

#include <jni.h>
#include "modules/video_coding/codecs/h264/include/h264.h"
#include "sdk/android/generated_libh264_jni/LibH264Decoder_jni.h"
#include "sdk/android/generated_libh264_jni/LibH264Encoder_jni.h"
#include "sdk/android/src/jni/video_codec_info.h"
#include "sdk/android/src/jni/jni_helpers.h"

namespace webrtc {
namespace jni {

static jlong JNI_LibH264Encoder_CreateEncoder(JNIEnv* jni, const
base::android::JavaParamRef<jobject>& codecInfo) {
  return jlongFromPointer(H264Encoder::Create(cricket::VideoCodec(
          VideoCodecInfoToSdpVideoFormat(jni, codecInfo))).release());
}

static jboolean JNI_LibH264Encoder_IsSupported(JNIEnv* jni) {
  return H264Encoder::IsSupported();
}

static jlong JNI_LibH264Decoder_CreateDecoder(JNIEnv* jni) {
  return jlongFromPointer(H264Decoder::Create().release());
}

static jboolean JNI_LibH264Decoder_IsSupported(JNIEnv* jni) {
  return H264Decoder::IsSupported();
}

}  // namespace jni
}  // namespace webrtc
