package com.tara.util.main;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
import com.tara.util.persistence.node.NodeType;
import com.tara.util.persistence.node.config.FileConfig;
import com.tara.util.persistence.repo.AsyncRepository;
import com.tara.util.persistence.repo.Repository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class InfoClass {
    private InfoClass() {
    }

    public static void main(String[] args) throws InterruptedException {
        log.info(
            "This is the main class for java utils."
                + "\nThe function main() displays this information."
                + "\nThere is no other purpose of this Class."
        );
        FileConfig config = FileConfig.builder()
            .withNodePath("D:\\dev\\Utils\\Utils Common\\src\\main\\resources\\json")
            .build();

        Repository<TestVO> repository = new AsyncRepository<>(TestVO.class, 1000, config);

        for (int i = 0; i < 100; i++) {
            TestVO vo = new TestVO(String.valueOf(i), List.of(1, 2, 3), i, new TestSubVO());
            UID id = new StringUID();
            repository.registerNode(id, NodeType.FILE);
            repository.commit(id, vo);
        }

        log.info(repository.checkoutAll().toString());
        Thread.sleep(5000);
    }
}
