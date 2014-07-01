package com.niw.niwandroidlibrary.utils;
import android.util.Log;

public class DebugLog {
    public final static boolean DEBUG = true;

    public static void logd(String message)
    {
        if (DEBUG)
        {
            String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();            
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            if (className.contains("$")) {
            	className = className.substring(0, className.lastIndexOf("$"));
            }
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.d("Gametest", "at ("+ className + ".java:" + lineNumber + ") " + "[" + methodName + "]" + message);
        }
    }
    public static void loge(String message)
    {
        if (DEBUG)
        {
        	String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();            
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            if (className.contains("$")) {
            	className = className.substring(0, className.lastIndexOf("$"));
            }
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.e("Gametest", "at ("+ className + ".java:" + lineNumber + ") " + "[" + methodName + "]" + message);
        }
    }
    
    public static void logi(String message)
    {
        if (DEBUG)
        {
        	String fullClassName = Thread.currentThread().getStackTrace()[3].getClassName();            
            String className = fullClassName.substring(fullClassName.lastIndexOf(".") + 1);
            if (className.contains("$")) {
            	className = className.substring(0, className.lastIndexOf("$"));
            }
            String methodName = Thread.currentThread().getStackTrace()[3].getMethodName();
            int lineNumber = Thread.currentThread().getStackTrace()[3].getLineNumber();

            Log.i("Gametest", "at ("+ className + ".java:" + lineNumber + ") " + "[" + methodName + "]" + message);
        }
    }
}
