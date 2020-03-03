package com.tara.util.main;

import com.tara.util.id.StringUID;
import com.tara.util.persistence.node.JSONNode;
import com.tara.util.persistence.node.config.JSONConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class InfoClass {
    private InfoClass() {
    }

    private List<List<List<List<List<String>>>>> list = new ArrayList<>();

    public static void main(String[] args) {
        log.info(
                "This is the main class for java utils."
                        + "\nThe function main() displays this information."
                        + "\nThere is no other purpose of this Class."
        );

        JSONConfig config = JSONConfig.builder().withNodePath("D:\\dev\\Utils\\Utils Common\\src\\main\\resources").build();
        JSONNode<TestVO> node = new JSONNode<>(new StringUID("Test"), TestVO.class, config);
        TestVO vo1 = new TestVO("Hello World!", List.of(1, 1, 2, 3, 5, 8, 13, 21), 123, new TestSubVO());
        log.info(vo1.toString());
        node.commit(vo1);
        node.push();
        node.fetch();
        TestVO vo2 = node.checkout();
        log.info(vo2.toString());
    }
}
