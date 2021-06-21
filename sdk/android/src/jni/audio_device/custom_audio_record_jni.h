#ifndef SDK_ANDROID_SRC_JNI_AUDIO_DEVICE_CUSTOM_AUDIO_RECORD_JNI_H_
#define SDK_ANDROID_SRC_JNI_AUDIO_DEVICE_CUSTOM_AUDIO_RECORD_JNI_H_

#include <jni.h>
#include <memory>

#include "modules/audio_device/audio_device_buffer.h"
#include "modules/audio_device/include/audio_device_defines.h"
#include "rtc_base/thread_checker.h"
#include "sdk/android/src/jni/audio_device/audio_device_module.h"

namespace webrtc {
    namespace jni {
        class CustomAudioRecordJni : public AudioInput {
        public:
            CustomAudioRecordJni(JNIEnv *env,
                                 const AudioParameters &audio_parameters,
                                 int total_delay_ms,
                                 const JavaRef<jobject> &j_webrtc_audio_record);

            ~CustomAudioRecordJni() override;

            int32_t Init() override;

            int32_t Terminate() override;

            int32_t InitRecording() override;

            bool RecordingIsInitialized() const override;

            int32_t StartRecording() override;

            int32_t StopRecording() override;

            bool Recording() const override;

            void AttachAudioBuffer(AudioDeviceBuffer *audioBuffer) override;

            bool IsAcousticEchoCancelerSupported() const override;

            bool IsNoiseSuppressorSupported() const override;

            int32_t EnableBuiltInAEC(bool enable) override;

            int32_t EnableBuiltInNS(bool enable) override;

            // Called from Java side so we can cache the address of the Java-manged
            // |byte_buffer| in |direct_buffer_address_|. The size of the buffer
            // is also stored in |direct_buffer_capacity_in_bytes_|.
            // This method will be called by the WebRtcAudioRecord constructor, i.e.,
            // on the same thread that this object is created on.
            void CacheDirectBufferAddress(JNIEnv *env,
                                          const JavaParamRef<jobject> &j_caller,
                                          const JavaParamRef<jobject> &byte_buffer);

            // Called periodically by the Java based WebRtcAudioRecord object when
            // recording has started. Each call indicates that there are |length| new
            // bytes recorded in the memory area |direct_buffer_address_| and it is
            // now time to send these to the consumer.
            // This method is called on a high-priority thread from Java. The name of
            // the thread is 'AudioRecordThread'.
            void DataIsRecorded(JNIEnv *env,
                                const JavaParamRef<jobject> &j_caller,
                                int length);

        private:
            // Stores thread ID in constructor.
            rtc::ThreadChecker thread_checker_;

            // Stores thread ID in first call to OnDataIsRecorded() from high-priority
            // thread in Java. Detached during construction of this object.
            rtc::ThreadChecker thread_checker_java_;

            // Wraps the Java specific parts of the AudioRecordJni class.
            JNIEnv *env_ = nullptr;
            ScopedJavaGlobalRef<jobject> j_audio_record_;

            const AudioParameters audio_parameters_;

            // Delay estimate of the total round-trip delay (input + output).
            // Fixed value set once in AttachAudioBuffer() and it can take one out of two
            // possible values. See audio_common.h for details.
            const int total_delay_ms_;

            // Cached copy of address to direct audio buffer owned by |j_audio_record_|.
            void *direct_buffer_address_;

            // Number of bytes in the direct audio buffer owned by |j_audio_record_|.
            size_t direct_buffer_capacity_in_bytes_;

            // Number audio frames per audio buffer. Each audio frame corresponds to
            // one sample of PCM mono data at 16 bits per sample. Hence, each audio
            // frame contains 2 bytes (given that the Java layer only supports mono).
            // Example: 480 for 48000 Hz or 441 for 44100 Hz.
            size_t frames_per_buffer_;

            bool initialized_;

            bool recording_;

            // Raw pointer handle provided to us in AttachAudioBuffer(). Owned by the
            // AudioDeviceModuleImpl class and called by AudioDeviceModule::Create().
            AudioDeviceBuffer *audio_device_buffer_;
        };

    }
}
#endif