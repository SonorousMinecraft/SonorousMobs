package com.sereneoasis.util;

import com.sereneoasis.ability.CoreAbility;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.util.Set;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static Set<Class<?>> findAllClasses(String packageName) {
        Reflections reflections = new Reflections(packageName, new SubTypesScanner(false));
        return reflections.getSubTypesOf(CoreAbility.class)
                .stream()
                .collect(Collectors.toSet());
    }
}
