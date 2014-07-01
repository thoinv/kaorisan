package com.kaorisan.common;
import android.util.Log;

public class DebugLog
{
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

            Log.d("KaoriSan", "at ("+ className + ".java:" + lineNumber + ") " + "[" + methodName + "]" + message);
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

            Log.e("KaoriSan", "at ("+ className + ".java:" + lineNumber + ") " + "[" + methodName + "]" + message);
        }
    }
}
