package org.venutolo.mp3.process.traits

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File

trait Mp3FileProcess {

    void validateMp3File(@Nonnull final Mp3File mp3File) {
        requireNonNull(mp3File, 'MP3 file cannot be null')
    }

    boolean shouldRunProcess(@Nonnull final Mp3File mp3File, final boolean requiresId3v2Tags) {
        !requiresId3v2Tags || (requiresId3v2Tags && mp3File.hasID3v2Tag())
    }

}