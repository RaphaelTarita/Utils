package com.tara.util.annotation.processor;

import com.tara.util.annotation.Scan;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class GenericScanner {
    private final Class<?> target;
    private List<Field> fields;
    private List<Method> methods;
    private List<Constructor<?>> constructors;
    private List<Class<?>> classes;
    private final ScannerType type;
    private final Class<? extends Annotation>[] enable;
    private final Class<? extends Annotation>[] disable;

    public static GenericScanner ignoreScanAnnotation(Class<?> target) {
        return new GenericScanner(
            target,
            DefaultScanValues.value,
            DefaultScanValues.enable,
            DefaultScanValues.disable
        );
    }

    public static GenericScanner ignoreScanAnnotation(Class<?> target, ScannerType type) {
        return new GenericScanner(
            target,
            type,
            DefaultScanValues.enable,
            DefaultScanValues.disable
        );
    }

    public static GenericScanner ignoreScanAnnotation(Class<?> target, Class<? extends Annotation>[] enable, Class<? extends Annotation>[] disable) {
        return new GenericScanner(
            target,
            DefaultScanValues.value,
            enable,
            disable
        );
    }

    public static GenericScanner ignoreScanAnnotation(Class<?> target, ScannerType type, Class<? extends Annotation>[] enable, Class<? extends Annotation>[] disable) {
        return new GenericScanner(
            target,
            type,
            enable,
            disable
        );
    }

    private GenericScanner(Class<?> target, ScannerType type, Class<? extends Annotation>[] enable, Class<? extends Annotation>[] disable) {
        this.target = target;
        this.type = type;
        this.enable = enable;
        this.disable = disable;
    }

    public GenericScanner(Class<?> target) {
        if (!target.isAnnotationPresent(Scan.class)) {
            throw new IllegalArgumentException("Class targeted by scanner (" + target.toString() + ") is not annotated with @Scan");
        }
        this.target = target;
        type = target.getAnnotation(Scan.class).value();
        enable = target.getAnnotation(Scan.class).enable();
        disable = target.getAnnotation(Scan.class).disable();
    }

    private <E extends AnnotatedElement> List<E> scanWith(Function<Class<?>, E[]> scanner, boolean recursive) {
        Class<?> clazz = target;
        List<E> result = new ArrayList<>();
        while (clazz != Object.class) {
            E[] raw = scanner.apply(clazz);
            for (E elem : raw) {
                if (visible(elem)) {
                    result.add(elem);
                }
            }
            if (!recursive) {
                break;
            }
            clazz = clazz.getSuperclass();
        }
        return result;
    }

    private <E extends AnnotatedElement> List<E> scanWith(Function<Class<?>, E[]> scanner) {
        return scanWith(scanner, true);
    }

    private boolean visible(AnnotatedElement elem) {
        switch (type) {
            case WHITELIST:
                for (Class<? extends Annotation> enabler : enable) {
                    if (elem.isAnnotationPresent(enabler)) {
                        return true;
                    }
                }
                return false;
            case BLACKLIST:
                for (Class<? extends Annotation> disabler : disable) {
                    if (elem.isAnnotationPresent(disabler)) {
                        return false;
                    }
                }
                return true;
            default:
                return false;
        }
    }

    public void scan() {
        fields = scanWith(Class::getDeclaredFields);
        methods = scanWith(Class::getDeclaredMethods);
        constructors = scanWith(Class::getDeclaredConstructors, false);
        classes = scanWith(Class::getClasses, false);
    }

    public void clear() {
        fields.clear();
        methods.clear();
        constructors.clear();
        classes.clear();
    }

    public void refresh() {
        clear();
        scan();
    }

    public List<Field> getFields() {
        return fields;
    }

    public List<Method> getMethods() {
        return methods;
    }

    public List<Constructor<?>> getConstructors() {
        return constructors;
    }

    public List<Class<?>> getNestedClasses() {
        return classes;
    }
}
