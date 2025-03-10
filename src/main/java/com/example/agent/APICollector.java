package com.example.agent;


import com.example.agent.utils.ReflectionUtils;
import org.json.JSONObject;

import java.io.FileWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class APICollector {
    public static void collectAPI(Object handlerMethod, Object mappingInfo) {
        try {
            Map<String, Object> apiInfo = new LinkedHashMap<>();

            // 获取路径
            Object patternsCondition = ReflectionUtils.invokeMethod(mappingInfo, "getPatternsCondition");
            Set<String> patterns = ReflectionUtils.invokeMethod(patternsCondition, "getPatterns");
            String path = patterns.iterator().next();

            // 获取HTTP方法
            Object methodsCondition = ReflectionUtils.invokeMethod(mappingInfo, "getMethodsCondition");
            Set<?> methods = ReflectionUtils.invokeMethod(methodsCondition, "getMethods");
            String httpMethod = methods.isEmpty() ? "GET" : methods.iterator().next().toString();

            // 获取处理方法信息
            Method method = ReflectionUtils.invokeMethod(handlerMethod, "getMethod");
            Class<?> controllerClass = ReflectionUtils.invokeMethod(handlerMethod, "getBeanType");

            // 处理参数
            List<Map<String, Object>> parameters = new ArrayList<>();
            for (Parameter param : method.getParameters()) {
                processParameter(param, parameters);
            }

            // 构建JSON结构
            apiInfo.put("path", path);
            apiInfo.put("method", httpMethod);
            apiInfo.put("parameters", parameters);
            apiInfo.put("controller", controllerClass.getName());
            apiInfo.put("handlerMethod", method.getName());

            // 写入文件
            writeToFile(apiInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void processParameter(Parameter param, List<Map<String, Object>> parameters) {
        // 示例：处理@RequestParam
        if (param.isAnnotationPresent(org.springframework.web.bind.annotation.RequestParam.class)) {
            org.springframework.web.bind.annotation.RequestParam annotation =
                    param.getAnnotation(org.springframework.web.bind.annotation.RequestParam.class);
            Map<String, Object> paramInfo = new LinkedHashMap<>();
            paramInfo.put("in", "query");
            paramInfo.put("name", annotation.value());
            paramInfo.put("required", annotation.required());
            paramInfo.put("schema", Collections.singletonMap("type", "string"));
            parameters.add(paramInfo);
        }
        // 其他注解处理...
    }

    private static synchronized void writeToFile(Map<String, Object> apiInfo) {
        try (FileWriter file = new FileWriter("api-info.json", true)) {
            file.write(new JSONObject(apiInfo).toString() + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}