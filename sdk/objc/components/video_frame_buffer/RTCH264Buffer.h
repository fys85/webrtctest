/*
 * RTCH264Buffer.h
 */
#import "RTCMacros.h"
#import "RTCVideoFrameBuffer.h"

NS_ASSUME_NONNULL_BEGIN

RTC_OBJC_EXPORT
@interface RTCH264Buffer : NSObject <RTCVideoFrameBuffer>

@property (nonatomic, readonly) NSData *h264Buffer;
@property (nonatomic, readonly) BOOL isKeyframe;

- (instancetype)initWithH264Buffer:(NSData * _Nonnull)h264Buffer 
				 			 width:(int)width
							height:(int)height
						isKeyframe:(BOOL)isKeyframe;
@end

NS_ASSUME_NONNULL_END
