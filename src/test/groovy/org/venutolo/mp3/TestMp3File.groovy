package org.venutolo.mp3

import javax.annotation.Nonnull
import javax.annotation.Nullable
import org.venutolo.mp3.core.Id3v1Tag
import org.venutolo.mp3.core.Id3v2Tag
import org.venutolo.mp3.core.Mp3File

class TestMp3File implements Mp3File {

    @Nonnull private final File file
    @Nullable private Id3v1Tag id3v1Tag = null
    @Nullable private Id3v2Tag id3v2Tag = null

    TestMp3File(@Nonnull final File file) {
        this.file = file
    }

    @Override
    @Nonnull
    File getFile() {
        this.file
    }

    @Override
    @Nullable
    Id3v1Tag getId3v1Tag() {
        this.id3v1Tag
    }

    @Override
    void setId3v1Tag(@Nonnull final Id3v1Tag tag) {
        this.id3v1Tag = tag
    }

    @Override
    void removeId3v1Tag() {
        this.id3v1Tag = null
    }

    @Override
    @Nullable
    Id3v2Tag getId3v2Tag() {
        this.id3v2Tag
    }

    @Override
    void setId3v2Tag(@Nonnull final Id3v2Tag tag) {
        this.id3v2Tag = tag
    }

}
