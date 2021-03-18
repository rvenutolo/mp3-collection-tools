package org.venutolo.mp3.specs

import static java.util.Objects.requireNonNull
import java.nio.file.Files
import javax.annotation.Nonnull

class TempDirFileCopyUtil {

    private static final File RESOURCE_DIR = new File('src/test/resources')

    @Nonnull private final File tempDir

    TempDirFileCopyUtil(@Nonnull final File tempDir) {
        requireNonNull(tempDir, 'Temp dir is null')
        if (!tempDir.exists()) {
            throw new IllegalArgumentException('Temp dir does not exist')
        }
        this.tempDir = tempDir
    }

    File copy(@Nonnull final String srcName, @Nonnull final String destName) {
        copy(new File(RESOURCE_DIR, srcName), destName)
    }

    File copy(@Nonnull final File srcFile, @Nonnull final String destName) {
        def destFile = new File(tempDir, destName)
        Files.copy(srcFile.toPath(), destFile.toPath())
        destFile
    }

}
