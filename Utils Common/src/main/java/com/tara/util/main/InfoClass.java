package com.tara.util.main;

import com.tara.util.id.StringUID;
import com.tara.util.id.UID;
import com.tara.util.persistence.http.HTTPConvert;
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
            .withURL("path/to/resource")
            .withIDpos(IDpos.REQUEST_URL)
            .build();
        log.info("\n" + HTTPConvert.getRequest(id, config, "id"));
    }
}
