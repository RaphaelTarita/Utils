package com.tara.util.main;

import com.tara.util.helper.nil.BasicNullResolver;
import com.tara.util.helper.nil.EqualsMode;
import com.tara.util.helper.nil.NullResolver;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InfoClass {
    private InfoClass() {
    }

    public static void main(String[] args) {
        log.info(
                "This is the main class for java utils."
                        + "\nThe function main() displays this information."
                        + "\nThere is no other purpose of this Class."
        );

        NullResolver resolver = BasicNullResolver.instance();
        boolean result = resolver.nullEquals(EqualsMode.ONE_NULL_TRUE, resolver, null, resolver, log, null);
        log.info(Boolean.toString(result));
    }
}
