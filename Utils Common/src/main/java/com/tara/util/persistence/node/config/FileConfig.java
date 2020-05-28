package com.tara.util.persistence.node.config;

import com.tara.util.mirror.Mirrors;

import java.io.File;
import java.nio.file.Path;

public class FileConfig implements NodeConfig {
    private final Path nodePath;
    private final String fileExt;

    private FileConfig(Path nodePath, String fileExt) {
        this.nodePath = nodePath;
        this.fileExt = fileExt;
    }

    public static class Builder implements ConfigBuilder<FileConfig> {
        private Path builderNodePath;
        private String builderFileExt;

        private Builder() {
            builderNodePath = Path.of("/resources/json");
            builderFileExt = ".json";
        }

        private Builder(FileConfig config) {
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

        @Override
        public FileConfig build() {
            return new FileConfig(builderNodePath, builderFileExt);
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
        return nodePath.resolve(filename + fileExt).toAbsolutePath().toFile();
    }

    @Override
    public Builder thisBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static FileConfig defaultConf() {
        return builder().build();
    }

    @Override
    public FileConfig mirror() {
        return thisBuilder().mirror().build();
    }
}