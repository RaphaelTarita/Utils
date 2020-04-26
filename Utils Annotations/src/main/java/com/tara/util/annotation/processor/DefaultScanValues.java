package com.tara.util.annotation.processor;

import com.tara.util.annotation.Scan;
import com.tara.util.annotation.ScannerType;

import java.lang.annotation.Annotation;

@Scan
class DefaultScanValues {
    private static final Scan SCAN = DefaultScanValues.class.getAnnotation(Scan.class);

    static final ScannerType value = SCAN.value();

    static final Class<? extends Annotation>[] enable = SCAN.enable();

    static final Class<? extends Annotation>[] disable = SCAN.disable();

    private DefaultScanValues() {
    }
}
