/*
 *  RTCH264Buffer.mm
 */

#import "RTCH264Buffer.h"

@implementation RTCH264Buffer

@synthesize h264Buffer = _h264Buffer;
@synthesize width = _width;
@synthesize height = _height;
@synthesize isKeyframe = _isKeyframe;

- (instancetype)initWithH264Buffer:(NSData * _Nonnull)h264Buffer 
               width:(int)width
              height:(int)height
            isKeyframe:(BOOL)isKeyframe {
  if (self = [super init]) {
    _h264Buffer = h264Buffer;
    _width = width;
    _height = height;
    _isKeyframe = isKeyframe;
  }

  return self;
}

- (void)dealloc {
  _h264Buffer = nil;
}

- (int)width {
  return _width;
}

- (int)height {
  return _height;
}

- (id<RTCI420Buffer>)toI420 {
  return nil;
}

@end
