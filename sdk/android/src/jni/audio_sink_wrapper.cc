#include "sdk/android/src/jni/audio_sink_wrapper.h"
#include "sdk/android/generated_java_audio_jni/AudioSink_jni.h"
#include "sdk/android/generated_java_audio_jni/AudioFrame_jni.h"

namespace webrtc {
    namespace jni {

        AudioSinkWrapper::AudioSinkWrapper(JNIEnv* jni, const JavaRef<jobject>& j_sink)
                : j_sink_(jni, j_sink) {}

        AudioSinkWrapper::~AudioSinkWrapper() {}

        void AudioSinkWrapper::OnData(const void* audio_data,
                                      int bits_per_sample,
                                      int sample_rate,
                                      size_t number_of_channels,
                                      size_t number_of_frames) {
            JNIEnv* jni = AttachCurrentThreadIfNeeded();
            ScopedJavaLocalRef<jobject> j_frame =
                    webrtc::jni::Java_AudioFrame_Constructor(jni, ScopedJavaLocalRef<jobject>(jni,
					    jni->NewDirectByteBuffer(const_cast<void *>(audio_data), number_of_frames * number_of_channels * bits_per_sample / 8)),
                                                             bits_per_sample, sample_rate, (int)number_of_channels, (int)number_of_frames);
            Java_AudioSink_onData(jni, j_sink_, j_frame);
        }


    }  // namespace jni
}  // namespace webrtc
