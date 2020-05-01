package com.tara.util.main;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
import com.tara.util.persistence.entity.JGPAEntity;
import com.tara.util.persistence.http.HTTPConvert;
import com.tara.util.persistence.http.HTTPHeader;
import com.tara.util.persistence.http.IDpos;
import com.tara.util.persistence.node.config.HTTPConfig;
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

        UID id = UID.mapString(StringUID.class, "test");
        HTTPConfig config = HTTPConfig.builder()
            .withAdditionalHeader(HTTPHeader.EXPIRES, "tomorrow")
            .withAdditionalParam("username", "user")
            .withAdditionalParam("password", "12345")
            .withURL("path/to/resource")
            .withIDpos(IDpos.REQUEST_PARAM)
            .build();
        TestVO vo = new TestVO();
        JGPAEntity<TestVO> entity = new JGPAEntity<>(vo);
        log.info("\n\n" + HTTPConvert.putRequest(id, entity, config));
    }
}
