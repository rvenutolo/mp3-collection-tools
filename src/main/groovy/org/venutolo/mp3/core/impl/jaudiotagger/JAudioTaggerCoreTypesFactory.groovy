package org.venutolo.mp3.core.impl.jaudiotagger

import javax.annotation.Nonnull
import org.venutolo.mp3.core.CoreTypesFactory
import org.venutolo.mp3.core.Id3v1Tag
import org.venutolo.mp3.core.Id3v2Tag
import org.venutolo.mp3.core.Id3v2Tag.Version
import org.venutolo.mp3.core.Mp3File

class JAudioTaggerCoreTypesFactory implements CoreTypesFactory {

    @Nonnull
    Mp3File newMp3File(@Nonnull final File file) {
        new JAudioTaggerMp3File(file)
    }

    @Nonnull
    Id3v1Tag newId3v1Tag() {
        new JAudioTaggerId3v1Tag()
    }

    @Nonnull
    Id3v2Tag newId3v2Tag(@Nonnull final Version version) {
        new JAudioTaggerId3v2Tag(version)
    }

}
