package com.example.utils;
/**
 * ȷ��һ��ʱ��������һ�ε����Ч
 * @author dell
 *
 */
public class OneClickUtils {
    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();   
        if ( time - lastClickTime < 500) {   
            return true;   
        }   
        lastClickTime = time;   
        return false;   
    }
}
