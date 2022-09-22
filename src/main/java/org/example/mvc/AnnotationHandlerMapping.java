package org.example.mvc;

import org.example.mvc.annotation.RequestMapping;
import org.reflections.Reflections;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class AnnotationHandlerMapping implements HandlerMapping {
    private final Object[] basePackage;
    private Map<HandlerKey, AnnotationHandler> handlers = new HashMap<>();

    public AnnotationHandlerMapping(Object... basePackage) {
        this.basePackage = basePackage;
    }

    public void initialize() {
        Reflections reflections = new Reflections(basePackage);

        Set<Class<?>> clazzWithControllerAnnotation = reflections.getTypesAnnotatedWith(org.example.mvc.annotation.Controller.class);

        clazzWithControllerAnnotation.forEach(
                clazz -> Arrays.stream(clazz.getDeclaredMethods())
                        .forEach(method -> {
                            RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

                            Arrays.stream(requestMapping.method())
                                    .forEach(restMethod -> {
                                        handlers.put(new HandlerKey(restMethod, requestMapping.value()), new AnnotationHandler(clazz, method));
                                    });

                        })
        );
    }

    @Override
    public Object findHandler(HandlerKey handlerKey) {
        return handlers.get(handlerKey);
    }
}
