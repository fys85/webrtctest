/*
 * RTCRGBTextureCache.h
 */

#import <GLKit/GLKit.h>

@class RTCVideoFrame;

NS_ASSUME_NONNULL_BEGIN

@interface RTCRGBTextureCache : NSObject

@property(nonatomic, readonly) GLuint rgbTexture;

- (instancetype)init NS_UNAVAILABLE;
- (nullable instancetype)initWithContext:(EAGLContext *)context NS_DESIGNATED_INITIALIZER;

- (BOOL)uploadFrameToTextures:(RTCVideoFrame *)frame;

- (void)releaseTextures;

@end

NS_ASSUME_NONNULL_END
