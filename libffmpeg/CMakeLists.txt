# For more information about using CMake with Android Studio, read the
# documentation: https://d.android.com/studio/projects/add-native-code.html

# Sets the minimum version of CMake required to build the native library.

#CMake版本信息
cmake_minimum_required(VERSION 3.4.1)

#支持-std=gnu++11
set(CMAKE_VERBOSE_MAKEFILE on)

message("--I print it:----------->>>>>>>'${CMAKE_CXX_FLAGS}'")

project( libFFmpeg )

message("Checking CMAKE_SYSTEM_NAME = '${CMAKE_SYSTEM_NAME}'")
if (${CMAKE_SYSTEM_NAME} MATCHES "Darwin")
  add_definitions(-DOS_OSX)
elseif (${CMAKE_SYSTEM_NAME} MATCHES "Linux")
  add_definitions(-DOS_LINUX)
elseif (${CMAKE_SYSTEM_NAME} MATCHES "Windows")
  add_definitions(-DOS_WIN)
elseif (${CMAKE_SYSTEM_NAME} MATCHES "Android")
  add_definitions(-DOS_ANDROID)
  message("Checking CMAKE_ABI_NAME = '${CMAKE_ANDROID_ARCH_ABI}'")
else()
  message("OS not detected.")
endif()

### 配置资源目录
add_library( MyFFmpeg
             SHARED
             src/main/cpp/native-lib.cpp )

find_library( log-lib
              log )


find_library( android-lib
              android )

set(distribution_DIR ${CMAKE_SOURCE_DIR}/src/main/jniLibs/${ANDROID_ABI})

add_library( avutil-55
             SHARED
             IMPORTED )
set_target_properties( avutil-55
                       PROPERTIES IMPORTED_LOCATION
                       ${distribution_DIR}/libavutil-55.so)

add_library( swresample-2
             SHARED
             IMPORTED )
set_target_properties( swresample-2
                       PROPERTIES IMPORTED_LOCATION
                       ${distribution_DIR}/libswresample-2.so)

add_library( avfilter-6
             SHARED
             IMPORTED )
set_target_properties( avfilter-6
                       PROPERTIES IMPORTED_LOCATION
                       ${distribution_DIR}/libavfilter-6.so)

add_library( avformat-57
             SHARED
             IMPORTED )
set_target_properties( avformat-57
                       PROPERTIES IMPORTED_LOCATION
                       ${distribution_DIR}/libavformat-57.so)

add_library( swscale-4
             SHARED
             IMPORTED )
set_target_properties( swscale-4
                       PROPERTIES IMPORTED_LOCATION
                       ${distribution_DIR}/libswscale-4.so)


add_library( avcodec-57
             SHARED
             IMPORTED )
set_target_properties( avcodec-57
                       PROPERTIES IMPORTED_LOCATION
                       ${distribution_DIR}/libavcodec-57.so)

set(CMAKE_VERBOSE_MAKEFILE on)
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11")
include_directories(src/main/cpp)
include_directories(src/main/jniLibs/include)

target_link_libraries(MyFFmpeg
                      avcodec-57
                      avfilter-6
                      avformat-57
                      avutil-55
                      swresample-2
                      swscale-4
                      ${log-lib}
                      ${android-lib})