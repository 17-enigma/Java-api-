# **Java Agent API 捕获工具项目文档**

## **1. 项目概述**
### **1.1 项目名称**
Java Agent API 捕获工具

### **1.2 项目简介**
本项目通过 Java Agent 技术，动态捕获目标 Java Web 应用的 API 信息（包括路径、HTTP 方法、参数等），并将捕获的信息以 JSON 格式保存到本地文件中。支持启动时和运行时动态附加 Agent。

### **1.3 项目目标**
- 实现对目标应用的 API 信息捕获。
- 支持启动时和运行时动态附加 Agent。
- 将捕获的 API 信息以 JSON 格式保存到本地文件。

  项目结构
javaweb-vuln-agent/
├── src
│   └── main
│       ├── java
│       │   └── com
│       │       └── example
│       │           └── agent
│       │               ├── Agent.java
│       │               ├── APITransformer.java
│       │               ├── APICollector.java
│       │               └── utils
│       │                   └── ReflectionUtils.java
│       └── resources
│           ├── logback.xml
│           └── META-INF
│               └── MANIFEST.MF
├── pom.xml
└── README.md

## **4. 核心功能**
### **4.1 Java Agent 入口**
- **类名**：`Agent.java`
- **功能**：
  - 实现 `premain` 和 `agentmain` 方法，支持启动时和运行时动态附加 Agent。
  - 注册字节码转换器 `APITransformer`。

### **4.2 字节码转换器**
- **类名**：`APITransformer.java`
- **功能**：
  - 实现 `ClassFileTransformer` 接口，动态修改目标类的字节码。
  - 对 Spring 框架的 `MappingRegistry` 类进行字节码增强。

### **4.3 ASM 访问器**
- **类名**：`MappingRegistryVisitor.java`
- **功能**：
  - 使用 ASM 库访问和修改目标类的字节码。
  - 在 `register` 方法中插入数据收集逻辑。

### **4.4 数据收集器**
- **类名**：`APICollector.java`
- **功能**：
  - 解析 API 的路径、HTTP 方法、参数等信息。
  - 将 API 信息以 JSON 格式写入本地文件。

### **4.5 反射工具类**
- **类名**：`ReflectionUtils.java`
- **功能**：
  - 提供反射工具方法，简化对目标类的动态调用。
