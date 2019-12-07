package com.tara.util.main;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
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

        UID id1 = new StringUID();
        UID id2 = new StringUID("abc");
        UID id3 = new StringUID("abc");

        log.info(id1.toString() + ", " + id2.toString() + ", " + id3.toString());
    }
}
