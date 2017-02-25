package com.careem.commons;

import org.springframework.context.ApplicationContext;

public class BeanUtil {
    public static <T> T getBean(Class<T> klass) {
        ApplicationContext applicationContext = new ApplicationContextProvider().getApplicationContext();
        return applicationContext.getBean(klass);
    }
}
