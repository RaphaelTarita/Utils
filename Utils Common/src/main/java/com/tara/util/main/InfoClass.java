package com.tara.util.main;

import com.tara.util.id.StringUID;
import com.tara.util.persistence.http.general.IDpos;
import com.tara.util.persistence.node.HTTPNode;
import com.tara.util.persistence.node.config.HTTPConfig;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class InfoClass {
    private InfoClass() {
    }

    public static void main(String[] args) throws IOException {
        log.info(
            "This is the main class for java utils."
                + "\nThe function main() displays this information."
                + "\nThere is no other purpose of this Class."
        );

        TestVO vo = new TestVO();

        HTTPConfig config = HTTPConfig.builder()
            .withIP("www.tdsoft.at")
            .withPort(8080)
            .withURL("/jgpa")
            .withIDpos(IDpos.REQUEST_URL)
            .build();

        HTTPNode<TestVO> node = new HTTPNode<>(new StringUID("test"), TestVO.class, config);

        node.commit(vo);
        node.push();
        node.fetch();
        try {
            vo = node.checkout();
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        node.close();
        log.info("\n\n" + node.getState().printHistory());
        log.info(vo.toString());
    }
}
