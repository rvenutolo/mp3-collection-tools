package org.venutolo.mp3.core

import static org.venutolo.mp3.core.Constants.ID3V2_TARGET_VERSION

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Id3v2Tag.Version

interface CoreTypesFactory {

    @Nonnull
    Mp3File newMp3File(@Nonnull final File file)

    @Nonnull
    Id3v1Tag newId3v1Tag()

    @Nonnull
    default Id3v2Tag newId3v2Tag() {
        newId3v2Tag(ID3V2_TARGET_VERSION)
    }

    @Nonnull
    Id3v2Tag newId3v2Tag(@Nonnull final Version version)

}