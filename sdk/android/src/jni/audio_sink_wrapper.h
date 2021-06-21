//
// Created by yong.zhao on 2021-02-25.
//

#ifndef ANDROID_WEBRTC_AUDIO_SINK_H
#define ANDROID_WEBRTC_AUDIO_SINK_H
#include <jni.h>

#include "api/media_stream_interface.h"
#include "sdk/android/src/jni/jni_helpers.h"

namespace webrtc {
namespace jni {
class AudioSinkWrapper : public webrtc::AudioTrackSinkInterface {
public:
    AudioSinkWrapper(JNIEnv* jni, const JavaRef<jobject>& j_sink);
    ~AudioSinkWrapper() override;

private:
    void OnData(const void* audio_data,
                int bits_per_sample,
                int sample_rate,
                size_t number_of_channels,
                size_t number_of_frames) override;

    const ScopedJavaGlobalRef<jobject> j_sink_;
};

    ScopedJavaLocalRef<jobject> NativeToJavaAudioFrame(JNIEnv* jni,
                                                       const void* audio_data,
                                                       int bits_per_sample,
                                                       int sample_rate,
                                                       size_t number_of_channels,
                                                       size_t number_of_frames);

}  // namespace jni
}  // namespace webrtc

#endif //ANDROID_WEBRTC_AUDIO_SINK_H
