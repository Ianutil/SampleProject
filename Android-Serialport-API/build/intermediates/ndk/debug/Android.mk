LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := serial_port
LOCAL_LDFLAGS := -Wl,--build-id
LOCAL_SRC_FILES := \
	/Users/ianchang/Public/Android/Workspace/MyApplication/Android-Serialport-API/src/main/jni/Android.mk \
	/Users/ianchang/Public/Android/Workspace/MyApplication/Android-Serialport-API/src/main/jni/Application.mk \
	/Users/ianchang/Public/Android/Workspace/MyApplication/Android-Serialport-API/src/main/jni/gen_SerialPort_h.sh \
	/Users/ianchang/Public/Android/Workspace/MyApplication/Android-Serialport-API/src/main/jni/SerialPort.c \

LOCAL_C_INCLUDES += /Users/ianchang/Public/Android/Workspace/MyApplication/Android-Serialport-API/src/main/jni
LOCAL_C_INCLUDES += /Users/ianchang/Public/Android/Workspace/MyApplication/Android-Serialport-API/src/debug/jni

include $(BUILD_SHARED_LIBRARY)
