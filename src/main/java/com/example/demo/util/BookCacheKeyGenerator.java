/**
 * @author  Ruvini Ramawickrama
 */
package com.example.demo.util;

import org.springframework.cache.interceptor.KeyGenerator;

import java.lang.reflect.Method;

public class BookCacheKeyGenerator implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "_"
                + params[0]; // params[0] => key
    }
}
