package org.venutolo.mp3.traits

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull

trait DirProcess {

    void validateDir(@Nonnull final File dir) {
        requireNonNull(dir, 'Directory cannot be null')
        if (dir.isFile()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
    }

    boolean shouldRunProcess(@Nonnull final File dir, final boolean requiresMp3Files) {
        def hasMp3s = dir.listFiles().any { file -> file.name.toLowerCase().endsWith('.mp3') }
        !requiresMp3Files || (requiresMp3Files && hasMp3s)
    }

}