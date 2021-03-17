package org.venutolo.mp3.process.traits

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File

trait MultipleMp3FileProcess {

    void validateMp3FilesAndDir(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir) {
        requireNonNull(mp3Files, 'MP3 files cannot be null')
        requireNonNull(dir, 'Directory cannot be null')
        if (mp3Files.isEmpty()) {
            throw new IllegalArgumentException('MP3 files cannot be empty')
        }
        if (dir.isFile()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
    }

    boolean shouldRunProcess(@Nonnull final Collection<Mp3File> mp3Files, final boolean requiresId3v2Tags) {
        // TODO make sure this all-requires-id3v2 is tested everywhere
        !requiresId3v2Tags || mp3Files.every { mp3File -> mp3File.hasId3v2Tag() }
    }

}