package com.function.ianchang.simplemvp;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.function.ianchang.simplemvp.base.ContactPresenter;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        Log.d("TAG", "包名:"+appContext.getPackageName());
        System.out.println("包名:"+appContext.getPackageName());


        boolean flag = ToolUtils.isConnected(appContext);
        Log.d("TAG", "ToolUtils.isConnected():flag="+flag);

        assertEquals("com.function.ianchang.simplemvp.test", flag);
    }
}
