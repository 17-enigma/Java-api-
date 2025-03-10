package com.example.agent.utils;

import java.lang.reflect.Method;

public class ReflectionUtils {
    public static <T> T invokeMethod(Object target, String methodName, Object... args) {
        try {
            Method method = target.getClass().getMethod(methodName);
            method.setAccessible(true);
            return (T) method.invoke(target, args);
        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke method: " + methodName, e);
        }
    }
}