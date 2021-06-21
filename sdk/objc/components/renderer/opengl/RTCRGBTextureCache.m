/*
 * RTCRGBTextureCache.m
 */

#import "RTCRGBTextureCache.h"

#import "base/RTCVideoFrame.h"
#import "base/RTCVideoFrameBuffer.h"
#import "components/video_frame_buffer/RTCCVPixelBuffer.h"

@implementation RTCRGBTextureCache {
  CVOpenGLESTextureCacheRef _textureCache;
  CVOpenGLESTextureRef _rgbTextureRef;
}

- (GLuint)rgbTexture {
  return CVOpenGLESTextureGetName(_rgbTextureRef);
}

- (instancetype)initWithContext:(EAGLContext *)context {
  if (self = [super init]) {
    CVReturn ret = CVOpenGLESTextureCacheCreate(
        kCFAllocatorDefault, NULL,
#if COREVIDEO_USE_EAGLCONTEXT_CLASS_IN_API
        context,
#else
        (__bridge void *)context,
#endif
        NULL, &_textureCache);
    if (ret != kCVReturnSuccess) {
      self = nil;
    }
  }
  return self;
}

- (BOOL)loadTexture:(CVOpenGLESTextureRef *)textureOut
        pixelBuffer:(CVPixelBufferRef)pixelBuffer
        pixelFormat:(GLenum)pixelFormat {
  const int width = CVPixelBufferGetWidth(pixelBuffer);
  const int height = CVPixelBufferGetHeight(pixelBuffer);

  if (*textureOut) {
    CFRelease(*textureOut);
    *textureOut = nil;
  }
  CVReturn ret = CVOpenGLESTextureCacheCreateTextureFromImage(
      kCFAllocatorDefault, _textureCache, pixelBuffer, NULL, GL_TEXTURE_2D, pixelFormat, width,
      height, pixelFormat, GL_UNSIGNED_BYTE, 0, textureOut);
  if (ret != kCVReturnSuccess) {
    if (*textureOut) {
      CFRelease(*textureOut);
      *textureOut = nil;
    }
    return NO;
  }
  NSAssert(CVOpenGLESTextureGetTarget(*textureOut) == GL_TEXTURE_2D,
           @"Unexpected GLES texture target");
  glBindTexture(GL_TEXTURE_2D, CVOpenGLESTextureGetName(*textureOut));
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
  glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
  return YES;
}

- (BOOL)uploadFrameToTextures:(RTCVideoFrame *)frame {
  NSAssert([frame.buffer isKindOfClass:[RTCCVPixelBuffer class]],
           @"frame must be CVPixelBuffer backed");
  RTCCVPixelBuffer *rtcPixelBuffer = (RTCCVPixelBuffer *)frame.buffer;
  CVPixelBufferRef pixelBuffer = rtcPixelBuffer.pixelBuffer;
  return [self loadTexture:&_rgbTextureRef
               pixelBuffer:pixelBuffer
               pixelFormat:GL_RGBA];
}

- (void)releaseTextures {
  if (_rgbTextureRef) {
    CFRelease(_rgbTextureRef);
    _rgbTextureRef = nil;
  }
}

- (void)dealloc {
  [self releaseTextures];
  if (_textureCache) {
    CFRelease(_textureCache);
    _textureCache = nil;
  }
}

@end
