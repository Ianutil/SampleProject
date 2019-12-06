//
// Created by Ian chang on 2018/1/25.
//

#ifndef MYAPPLICATION_FFMPEGHEADER_H
#define MYAPPLICATION_FFMPEGHEADER_H

#define METHOD_NAME(name) Java_com_function_ianchang_libffmpeg_FFmpegUtils_##name


#define  LOG_TAG    "My_FFmpeg" // 这个是自定义的LOG的标识

#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG ,__VA_ARGS__) // 定义LOGD类型
#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO, LOG_TAG ,__VA_ARGS__) // 定义LOGI类型
#define  LOGW(...)  __android_log_print(ANDROID_LOG_WARN, LOG_TAG ,__VA_ARGS__) // 定义LOGW类型
#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR, LOG_TAG ,__VA_ARGS__) // 定义LOGE类型
#define  LOGF(...)  __android_log_print(ANDROID_LOG_FATAL, LOG_TAG ,__VA_ARGS__) // 定义LOGF类型



#include <jni.h>
#include <string>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>

extern "C" {
#include "libavformat/avformat.h"
#include "libswscale/swscale.h"
#include <libavfilter/avfiltergraph.h>
#include "libavfilter/avfilter.h"
#include "libavutil/imgutils.h"
#include "libavutil/avutil.h"
#include "libavfilter/buffersink.h"
#include "libavfilter/buffersrc.h"
#include "libavcodec/avcodec.h"
}

#endif //MYAPPLICATION_FFMPEGHEADER_H
