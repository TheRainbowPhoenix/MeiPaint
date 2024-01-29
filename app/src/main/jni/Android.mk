LOCAL_PATH := $(call my-dir)

ifeq ($(TARGET_ARCH_ABI),armeabi-v7a)
PREFIX = $(PREFIX32)
endif
ifeq ($(TARGET_ARCH_ABI),arm64-v8a)
PREFIX = $(PREFIX64)
endif


include $(CLEAR_VARS)
LOCAL_MODULE := libNativeLib
#LOCAL_SRC_FILES :=  lib/$(TARGET_ARCH_ABI)/libNativeLib.so
LOCAL_SRC_FILES := ../jniLibs/$(TARGET_ARCH_ABI)/libNativeLib.so
include $(PREBUILT_SHARED_LIBRARY)