package com.tara.util.annotation;

import com.tara.util.annotation.processor.ScannerType;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Scan {

    ScannerType value() default ScannerType.BLACKLIST;

    Class<? extends Annotation>[] enable() default Select.class;

    Class<? extends Annotation>[] disable() default Ignore.class;
}
