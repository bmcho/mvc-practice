package org.example.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class AnnotationHandler {
    private final Method method;
    private final Class<?> clazz;

    public AnnotationHandler(Class<?> clazz, Method method) {
        this.clazz = clazz;
        this.method = method;
    }

    public String handle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Constructor<?> declaredConstruct = clazz.getDeclaredConstructor();
        Object handler = declaredConstruct.newInstance();

        return (String) method.invoke(handler, request, response);
    }
}
