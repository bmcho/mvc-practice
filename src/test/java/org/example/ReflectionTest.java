package org.example;

/*
 * @Controller 애노테이션이 설정돼 있는 모든 클래스를 찾아서 출력한다.
 */

import org.example.annotation.Controller;
import org.example.annotation.Service;
import org.example.mvc.model.User;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ReflectionTest {

    private static final Logger logger = LoggerFactory.getLogger(ReflectionTest.class);

    @Test
    void controllerScan() {
        Set<Class<?>> beans = getTypesAnnotatedWith(List.of(Controller.class, Service.class));
        logger.debug("beans: [{}]", beans);
    }

    @Test
    void givenObjUser_whenCheckingUserClass_thenDoseNotAnyExceptions() {
        Class<User> clazz = User.class;

        logger.debug(clazz.getName());
        logger.debug("User all declared fields [{}]", Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList()));
        logger.debug("User all declared constructors [{}]", Arrays.stream(clazz.getConstructors()).collect(Collectors.toList()));
        logger.debug("User all declared methods [{}]", Arrays.stream(clazz.getMethods()).collect(Collectors.toList()));

        assertThat(clazz.getName()).isEqualTo("org.example.model.User");
        assertThat(Arrays.stream(clazz.getDeclaredFields()).collect(Collectors.toList())).isNotEmpty();
        assertThat(Arrays.stream(clazz.getConstructors()).collect(Collectors.toList())).isNotEmpty();
        assertThat(Arrays.stream(clazz.getMethods()).collect(Collectors.toList())).isNotEmpty();
    }

    private static Set<Class<?>> getTypesAnnotatedWith(List<Class<? extends Annotation>> annotations) {
        Reflections reflections = new Reflections("org.example");

        Set<Class<?>> beans = new HashSet<>();
        annotations.forEach(annotation -> {
            beans.addAll(reflections.getTypesAnnotatedWith(annotation));
        });
        return beans;
    }
}
