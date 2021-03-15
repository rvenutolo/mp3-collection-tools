package org.venutolo.mp3.core

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import javax.annotation.Nullable
import org.jaudiotagger.audio.mp3.MP3File as WrappedMp3File

// TODO unit test
class Mp3File {

    @Nonnull private final WrappedMp3File wrappedFile

    Mp3File(@Nonnull final String filePath) {
        requireNonNull(filePath, "Path to MP3 file cannot be null")
        this.wrappedFile = new WrappedMp3File(filePath)
    }

    Mp3File(@Nonnull final File file) {
        requireNonNull(file, "File cannot be null")
        this.wrappedFile = new WrappedMp3File(file)
    }

    void setFile(@Nonnull final String filePath) {
        requireNonNull(filePath, "Path to MP3 file cannot be null")
        setFile(new File(filePath))
    }

    void setFile(@Nonnull final File file) {
        requireNonNull(file, "File cannot be null")
        wrappedFile.setFile(file)
    }

    @Nonnull
    File getFile() {
        wrappedFile.file
    }

    @Nonnull
    String getPath() {
        wrappedFile.file.canonicalPath
    }

    boolean hasID3v1Tag() {
        wrappedFile.hasID3v1Tag()
    }

    // TODO use optional?
    @Nullable
    ID3v1Tag getID3v1Tag() {
        wrappedFile.hasID3v1Tag() ? new ID3v1Tag(wrappedFile.getID3v1Tag()) : null
    }

    void setID3v1Tag(@Nonnull final ID3v1Tag tag) {
        requireNonNull(tag, "Tag cannot be null")
        wrappedFile.setID3v1Tag(tag.getWrappedTag())
    }

    boolean hasID3v2Tag() {
        wrappedFile.hasID3v2Tag()
    }

    // TODO use optional?
    @Nullable
    ID3v2Tag getID3v2Tag() {
        wrappedFile.hasID3v2Tag() ? new ID3v2Tag(wrappedFile.getID3v2Tag()) : null
    }

    void setID3v2Tag(@Nonnull final ID3v2Tag tag) {
        requireNonNull(tag, "Tag cannot be null")
        wrappedFile.setID3v2Tag(tag.getWrappedTag())
    }

    void removeID3v1Tag() {
        wrappedFile.setID3v1Tag(null)
    }

    void convertID3v2VersionTo24() {
        // TODO is there a better way to convert tag versions?
        wrappedFile.setID3v2Tag(wrappedFile.getID3v2TagAsv24())
    }

    String toString() {
        "${this.class.simpleName} [" +
            "path: ${getPath()}, " +
            "id3v1: ${getID3v1Tag()}, " +
            "id3v2: ${getID3v2Tag()}" +
            "]"
    }

}
