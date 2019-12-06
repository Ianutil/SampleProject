package com.example;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by ianchang on 2018/1/9.
 */

public class TestLoadJar {






    public static void main(String[] args){


        String path = "/Users/ianchang/Downloads/2017-11-23/eclipse/plugins";
        File file = new File(path);

        if (file.exists()){
            System.out.println("文件存在");
        }else{
            System.out.println("文件不存在");
        }


        File[] jars = file.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(File file, String name) {
                return name.endsWith(".jar") || name.endsWith(".zip");
            }
        });

        if (jars == null) return;

        System.out.println("有多少个Jar包:"+jars.length);

        ClassLoader loader = ClassLoader.getSystemClassLoader();

        URLClassLoader urlClassLoader = (URLClassLoader) loader;

        try {
            Method method = URLClassLoader.class.getDeclaredMethod("addURL", URL.class);

            if(!method.isAccessible()){
                System.out.println("无法调用该方法");

                method.setAccessible(true);
            }

            for (File jar : jars){
                System.out.println("name:"+jar.getName());

                URL url = jar.toURI().toURL();

                System.out.println("url:"+url.getPath());

                method.invoke(urlClassLoader, url);





                System.out.println("****************************");

            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }


}
