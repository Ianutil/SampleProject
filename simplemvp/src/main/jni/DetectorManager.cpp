#include <jni.h>

#include "DetectorManager.h"
#include <opencv2/opencv.hpp>

#include <dlib/image_processing/frontal_face_detector.h>
#include <dlib/image_processing/render_face_detections.h>
#include <dlib/image_processing.h>
#include <dlib/gui_widgets.h>
#include <dlib/image_io.h>
#include <dlib/opencv/cv_image.h>
#include <dlib/opencv.h>

#include <android/log.h>

#define TAG "Dlib-jni" // 这个是自定义的LOG的标识
#define LOGV(...) __android_log_print(ANDROID_LOG_VERBOSE, TAG, __VA_ARGS__)  // 定义LOGV类型
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

using namespace dlib;
using namespace std;
using namespace cv;

frontal_face_detector detector = get_frontal_face_detector();
shape_predictor sp;//定义个shape_predictor类的实例
cv::Rect box(0, 0, 0, 0);
std::vector<cv::Point2d> pts2d;		// 用于存储检测的点
array2d<rgb_pixel> img;//注意变量类型 rgb_pixel 三通道彩色图像
std::vector<full_object_detection> shapes;//注意形状变量的类型，full_object_detection
bool initflag = false;

#define DETECTOR_METHOD(METHOD_NAME) Java_com_function_ianchang_simplemvp_detector_DetectorManager_##METHOD_NAME

void init(string modelpath){
	LOGE("init");
	string model =modelpath+"/model/shape_predictor_68_face_landmarks.dat";
	deserialize(model) >> sp;

}

void detect(cv::Mat input){
	LOGE("detect");
	Mat result;
	cvtColor(input, result, CV_RGBA2BGR);
	assign_image(img, cv_image<bgr_pixel>(result));

	std::vector<dlib::rectangle> dets = detector(img);//检测人脸，获得边界框

	int Max = 0;
	int area = 0;
	if (dets.size() != 0)
	{
		for (unsigned long t = 0; t < dets.size(); ++t)
		{
			if (area < dets[t].width()*dets[t].height())
			{
				area = dets[t].width()*dets[t].height();
				Max = t;
			}
		}
	}

	full_object_detection shape = sp(img, dets[Max]);
	box.x = dets[Max].left();
	box.y = dets[Max].top();
	box.width = dets[Max].width();
	box.height = dets[Max].height();

	pts2d.clear();
	// 将结果保存至 pts2d 里面
	for (size_t k = 0; k < shape.num_parts(); k++) {
		Point2d p(shape.part(k).x(), shape.part(k).y());
		pts2d.push_back(p);
	}
}

//初始化dlib
JNIEXPORT jint  JNICALL DETECTOR_METHOD(initModel)
  (JNIEnv *env, jclass jobject,jstring path) {
	//获取绝对路径
	const char* modelPath;
	modelPath = env->GetStringUTFChars(path, 0);
	if(modelPath == NULL) {
		return 0;
	}
	string MPath = modelPath;

	LOGE("initModel");
	try {
		if(!initflag){
			init(MPath);
			initflag = true;
			return 1;
		}

	} catch (const std::exception &e) {

	} catch (...) {

	}
	return 0;
}

JNIEXPORT jstring JNICALL DETECTOR_METHOD(landMarks)
  (JNIEnv *env, jclass jobject, jlong intPtr, jlong outPtr) {
	Mat *inMat = (Mat*) intPtr;
	Mat *outMat =(Mat*) outPtr;
	*outMat = *inMat;

	LOGE("jnidetect");

	long start, finish;
	double totaltime;
	start = clock();

	try{
		detect(*outMat);
	} catch (const std::exception &e) {

	} catch (...) {

	}

	finish = clock();
	totaltime = (double)(finish - start) / CLOCKS_PER_SEC;
	LOGE("time = %f\n", totaltime*1000);
	string str = "Fail";
	if(pts2d.size() == 68){
		cv::rectangle(*outMat, box, Scalar(255, 0, 0), 2, 8, 0);

		for (int i = 0; i < 17; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		for (int i = 17; i < 27; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		for (int i = 27; i < 31; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		for (int i = 31; i < 36; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		for (int i = 36; i < 48; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		for (int i = 48; i < 60; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		for (int i = 60; i < 68; i++)	circle(*outMat, (pts2d)[i], 4, cv::Scalar(255, 0, 0), -1, 8, 0);
		str = "Success";
	}

    const char* ret = str.c_str();
    return env->NewStringUTF(ret);
}



