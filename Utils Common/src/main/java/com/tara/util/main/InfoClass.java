package com.tara.util.main;

import com.tara.util.container.collection.Table;
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

        int x = 5;
        int y = 5;

        Table<String> table = new Table<>(x, y);
        table.set(3, 3, "Hello ");
        table.set(2, 4, "World!");
        log.info(table.get(3, 3) + table.get(2, 4));
        for (int i = 0; i < y; i++) {
            log.info(table.getRow(i).toString());
        }
        log.info("(...)");
        for (int i = 0; i < x; i++) {
            log.info(table.getColumn(i).toString());
        }
    }
}
