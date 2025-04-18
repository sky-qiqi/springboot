package com.example.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

@Aspect // 标记这个类为一个切面
@Component // 使这个类成为 Spring 管理的 Bean
public class ServiceLoggingAspect {

    private static final ObjectMapper objectMapper = new ObjectMapper(); // Jackson 库的 ObjectMapper，用于将对象转换为 JSON

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // 可选：格式化输出 JSON
    }

    @Around("execution(public * com.example.service.UserService.*(..))") // 定义环绕通知，拦截 UserService 类所有 public 方法的执行
    public Object logServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName(); // 获取被调用方法的方法名
        Object[] args = joinPoint.getArgs(); // 获取被调用方法的参数数组
        String paramsJson = ""; // 用于存储参数的 JSON 字符串
        String resultJson = ""; // 用于存储返回值的 JSON 字符串
        long startTime = System.currentTimeMillis(); // 记录方法开始执行的时间
        Object result = null; // 存储方法执行结果
        Throwable exception = null; // 存储方法执行过程中抛出的异常

        try {
            paramsJson = objectMapper.writeValueAsString(Arrays.stream(args).map(this::convertToString).collect(Collectors.toList())); // 将参数列表转换为 JSON 字符串
            result = joinPoint.proceed(); // 执行目标方法
            resultJson = objectMapper.writeValueAsString(convertToString(result)); // 将返回值转换为 JSON 字符串
        } catch (Throwable e) {
            exception = e; // 捕获方法执行过程中抛出的异常
            throw e; // 重新抛出异常，以保持原始的行为
        } finally {
            long endTime = System.currentTimeMillis(); // 记录方法结束执行的时间
            long executionTime = endTime - startTime; // 计算方法执行耗时

            System.out.println("--------------------------------------------------");
            System.out.println("Method Name: " + methodName); // 打印方法名
            System.out.println("Parameters: " + paramsJson); // 打印参数 JSON
            System.out.println("Execution Time: " + executionTime + " ms"); // 打印执行耗时
            if (exception != null) {
                System.err.println("Exception Type: " + exception.getClass().getName()); // 打印异常类型
                System.err.println("Error Message: " + exception.getMessage()); // 打印错误信息
            } else {
                System.out.println("Return Value: " + resultJson); // 打印返回值 JSON
            }
            System.out.println("--------------------------------------------------");
        }
        return result;
    }

    private Object convertToString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj); // 尝试将对象转换为 JSON 字符串
        } catch (Exception e) {
            return String.valueOf(obj); // 如果转换失败（例如，对于基本类型），则使用对象的 toString() 方法
        }
    }
}