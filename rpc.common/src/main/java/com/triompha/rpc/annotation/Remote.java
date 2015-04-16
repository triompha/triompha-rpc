package com.triompha.rpc.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/***
 * 标记这个注解的时候，会将service注册到远程
 *
 * @author    triompha
 * @date 16 Apr, 2015
 * @see 
 * @since
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Remote {
}
