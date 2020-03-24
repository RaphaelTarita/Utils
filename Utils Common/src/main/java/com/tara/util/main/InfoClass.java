package com.tara.util.main;

import com.tara.util.async.tasks.SchedulerConfig;
import com.tara.util.container.list.BufferedArrayList;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;

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

        BufferedArrayList<Integer> list1 = new BufferedArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);
        list1.add(4);
        list1.add(5);
        list1.add(6);
        list1.add(7);
        list1.flush();
        list1.add(8);
        list1.add(9);
        list1.add(10);
        list1.add(11);
        list1.add(12);
        ArrayList<Integer> list2 = new ArrayList<>();
        list2.add(2);
        list2.add(5);
        list2.add(6);
        list2.add(11);

        list1.removeAll(list2);
        list1.flush();
        list1.flush();

        SchedulerConfig config = SchedulerConfig.builder()
                .withThreadName("Thread-1")
                .withRecoverCycle(3000)
                .withRetryCycle(1000)
                .build();
    }
}
