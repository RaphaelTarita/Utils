package com.tara.util.main;

import com.tara.util.mirror.Mirrors;
import com.tara.util.mirror.SimpleMirrorable;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InfoClass {

    public static void main(String[] args) {
        log.info(
                "This is the main class for java utils."
                        + "\nThe function main() displays this information."
                        + "\nThere is no other purpose of this Class."
        );

        List<List<SimpleMirrorable<String>>> list2d = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            List<SimpleMirrorable<String>> list = new ArrayList<>();
            for (int j = 0; j < 100; j++) {
                list.add(new SimpleMirrorable<>(j + " / " + i));
            }
            list2d.add(list);
        }

        log.info(list2d.toString());
        log.info(Mirrors.mirror(list2d).toString());
    }
}
