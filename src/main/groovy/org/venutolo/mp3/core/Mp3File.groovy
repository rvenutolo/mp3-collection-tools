package org.venutolo.mp3.core

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File as WrappedMp3File

// TODO unit test
class Mp3File {

    @Nonnull private final WrappedMp3File wrapped

    Mp3File(@Nonnull final String filePath) {
        requireNonNull(filePath, "Path to MP3 file cannot be null")
        this.wrapped = new WrappedMp3File(filePath)
    }

    Mp3File(@Nonnull final File file) {
        requireNonNull(file, "File cannot be null")
        this.wrapped = new WrappedMp3File(file)
    }

    void setFile(@Nonnull final String filePath) {
        requireNonNull(filePath, "Path to MP3 file cannot be null")
        setFile(new File(filePath))
    }

    void setFile(@Nonnull final File file) {
        requireNonNull(file, "File cannot be null")
        wrapped.setFile(file)
    }

    @Nonnull
    File getFile() {
        wrapped.file
    }

    @Nonnull
    String canonicalPath() {
        wrapped.file.canonicalPath
    }

    boolean hasID3v1Tag() {
        wrapped.hasID3v1Tag()
    }

    @Nonnull
    ID3v1Tag getID3v1Tag() {
        def wrappedTag = wrapped.getID3v1Tag()
        new ID3v1Tag(wrappedTag)
    }

    void setID3v1Tag(@Nonnull final ID3v1Tag tag) {
        requireNonNull(tag, "Tag cannot be null")
        wrapped.setID3v1Tag(tag.getWrappedTag())
    }

    boolean hasID3v2Tag() {
        wrapped.hasID3v2Tag()
    }

    @Nonnull
    ID3v2Tag getID3v2Tag() {
        def wrappedTag = wrapped.getID3v2Tag()
        new ID3v2Tag(wrappedTag)
    }

    void setID3v2Tag(@Nonnull final ID3v2Tag tag) {
        requireNonNull(tag, "Tag cannot be null")
        wrapped.setID3v2Tag(tag.getWrappedTag())
    }

    void removeID3v1Tag() {
        wrapped.setID3v1Tag(null)
    }

    void convertID3v2VersionTo24() {
        // TODO is there a better way to convert tag versions?
        wrapped.setID3v2Tag(wrapped.getID3v2TagAsv24())
    }

}
