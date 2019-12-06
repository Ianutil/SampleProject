// IBLPackageListener.aidl
package com.function.ianchang.simpleaidl;

// Declare any non-default types here with import statements
import com.function.ianchang.simpleaidl.BLPackageInfo;

interface IBLPackageListener {


    void setBLPackageInfo(in BLPackageInfo info);

    BLPackageInfo getBLPackageInfo();
}
