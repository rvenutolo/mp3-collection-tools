package org.venutolo.mp3.core

import static java.util.Objects.hash

import javax.annotation.Nonnull
import javax.annotation.Nullable

interface Mp3File {

    @Nonnull
    File getFile()

    @Nonnull
    default String getPath() {
        getFile().getCanonicalPath()
    }

    // TODO use optional?
    @Nullable
    Id3v1Tag getId3v1Tag()

    void setId3v1Tag(@Nonnull final Id3v1Tag tag)

    default boolean hasId3v1Tag() {
        getId3v1Tag()
    }

    void removeId3v1Tag()

    // TODO use optional
    @Nullable
    Id3v2Tag getId3v2Tag()

    void setId3v2Tag(@Nonnull final Id3v2Tag tag)

    default boolean hasId3v2Tag() {
        getId3v2Tag()
    }

    // TODO take in target version?
    void convertId3v2VersionTo24()

    @Override
    default String toString() {
        // TODO see which class name is used
        "MP3 [" +
            "path: ${getPath()}, " +
            "id3v1: ${getId3v1Tag()}, " +
            "id3v2: ${getId3v2Tag()}" +
            "]"
    }

    @Override
    default boolean equals(@Nonnull final Object o) {
        if (this.is(o)) {
            return true
        }
        if (getClass() != o.class) {
            return false
        }
        def that = (Mp3File) o
        if (this.getFile() != that.getFile()) {
            return false
        }
        if (this.getId3v1Tag() != that.getId3v1Tag()) {
            return false
        }
        if (this.getId3v2Tag() == that.getId3v2Tag()) {
            return false
        }
        return true
    }

    @Override
    default int hashCode() {
        hash(getFile(), getId3v1Tag(), getId3v2Tag())
    }

}