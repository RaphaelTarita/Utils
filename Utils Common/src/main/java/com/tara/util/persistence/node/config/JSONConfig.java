package com.tara.util.persistence.node.config;

import com.tara.util.mirror.Mirrorable;
import com.tara.util.mirror.Mirrors;

import java.io.File;
import java.nio.file.Path;

public class JSONConfig implements NodeConfig {
    private final Path nodePath;
    private final String fileExt;

    private JSONConfig(Path nodePath, String fileExt) {
        this.nodePath = nodePath;
        this.fileExt = fileExt;
    }

    public static class Builder implements Mirrorable<Builder> {
        private Path builderNodePath;
        private String builderFileExt;

        private Builder() {
            builderNodePath = Path.of("/resources/json");
            builderFileExt = ".json";
        }

        private Builder(JSONConfig config) {
            builderNodePath = config.nodePath;
            builderFileExt = config.fileExt;
        }

        public Builder withNodePath(Path nodePath) {
            builderNodePath = nodePath;
            return this;
        }

        public Builder withNodePath(String nodePath) {
            builderNodePath = Path.of(nodePath);
            return this;
        }

        public Builder withFileExt(String fileExt) {
            builderFileExt = (
                    fileExt.charAt(0) == '.'
                            ? ""
                            : '.'
            )
                    + fileExt;
            return this;
        }

        public JSONConfig build() {
            return new JSONConfig(builderNodePath, builderFileExt);
        }

        @Override
        public Builder mirror() {
            return (new Builder())
                    .withNodePath(Mirrors.mirror(builderNodePath))
                    .withFileExt(builderFileExt);
        }
    }

    public Path nodePath() {
        return nodePath;
    }

    public String fileExt() {
        return fileExt;
    }

    public File getFile(String filename) {
        return nodePath.resolve(filename + fileExt).toFile();
    }

    public Builder thisBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public JSONConfig mirror() {
        return thisBuilder().mirror().build();
    }
}
