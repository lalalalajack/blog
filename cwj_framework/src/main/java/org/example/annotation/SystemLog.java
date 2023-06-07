package org.example.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解   实现AOP切面增强  里的 切点
 * @Retention(RetentionPolicy.RUNTIME) 注解保持到runtime阶段，因为需要通过反射获取到注解
 * @Target({ElementType.METHOD}) 注解加在方法上
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SystemLog {
    String businessName();
}
