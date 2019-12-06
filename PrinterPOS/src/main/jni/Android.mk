
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

TARGET_PLATFORM := android-8
LOCAL_MODULE    := serial_port_ian
LOCAL_SRC_FILES := SerialPrinter.c
LOCAL_LDLIBS    := -llog

include $(BUILD_SHARED_LIBRARY)
