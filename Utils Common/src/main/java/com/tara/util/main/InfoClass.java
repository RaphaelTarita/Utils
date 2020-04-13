package com.tara.util.main;

import com.tara.util.persistence.node.FileNode;
import com.tara.util.persistence.node.config.FileConfig;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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

        TestVO vo = new TestVO("hello", List.of(1, 2, 3, 4, 5, 6, 7, 8), 27, new TestSubVO());
        log.info(vo.toString());

        FileConfig config = FileConfig.builder()
            .withNodePath("D:\\dev\\Utils\\Utils Common\\src\\main\\resources")
            .build();
        FileNode<TestVO> fileNode = new FileNode<>(TestVO.class, config);

        fileNode.pushthrough(vo);
        vo = fileNode.pull();

        log.info(vo.toString());
    }
}
