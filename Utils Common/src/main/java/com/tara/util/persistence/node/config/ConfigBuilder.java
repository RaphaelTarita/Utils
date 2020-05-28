package com.tara.util.persistence.node.config;

import com.tara.util.mirror.Mirrorable;

public interface ConfigBuilder<C extends NodeConfig> extends Mirrorable<ConfigBuilder<C>> {
    C build();
}