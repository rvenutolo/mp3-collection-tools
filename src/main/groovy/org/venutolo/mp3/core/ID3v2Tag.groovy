package org.venutolo.mp3.core

import static java.util.Objects.requireNonNull
import static org.venutolo.mp3.core.ID3v2Tag.Version.V2_4

import javax.annotation.Nonnull
import static org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile
import org.jaudiotagger.tag.id3.AbstractID3v2Tag as WrappedAbstractID3v2Tag
import org.jaudiotagger.tag.id3.ID3v22Tag as WrappedID3v22Tag
import org.jaudiotagger.tag.id3.ID3v23Tag as WrappedID3v23Tag
import org.jaudiotagger.tag.id3.ID3v24Tag as WrappedId3v24Tag

// TODO unit test
class ID3v2Tag extends AbstractID3Tag<WrappedAbstractID3v2Tag> {

    static enum Version {
        V2_4('2.4', WrappedId3v24Tag),
        V2_3('2.3', WrappedID3v23Tag),
        V2_2('2.3', WrappedID3v22Tag)

        private final String desc
        private final Class<WrappedAbstractID3v2Tag> clazz

        private Version(@Nonnull final String desc, @Nonnull final Class<WrappedAbstractID3v2Tag> clazz) {
            this.desc = desc
            this.clazz = clazz
        }

        String toString() {
             desc
        }

        private static Version from(@Nonnull final WrappedAbstractID3v2Tag instance) {
            def foundVersion = values().find { version -> version.clazz == instance.class }
            if (!foundVersion) {
                throw new IllegalArgumentException("Unexpected class: ${instance.class}")
            }
            foundVersion
        }
    }

    @Nonnull private final WrappedAbstractID3v2Tag wrappedTag
    @Nonnull private final Version version

    ID3v2Tag() {
        this(V2_4)
    }

    ID3v2Tag(@Nonnull final Version version) {
        requireNonNull(version, 'Version cannot be null')
        this.wrappedTag = version.clazz.newInstance()
        this.version = version
    }

    protected ID3v2Tag(@Nonnull private final WrappedAbstractID3v2Tag wrappedTag) {
        requireNonNull(wrappedTag, 'Wrapped tag cannot be null')
        this.wrappedTag = wrappedTag
        this.version = Version.from(wrappedTag)
    }

    @Override
    @Nonnull
    protected WrappedAbstractID3v2Tag getWrappedTag() {
        wrappedTag
    }

    void setArtwork(@Nonnull final File file) {
        requireNonNull(file, 'Artwork file cannot be null')
        wrappedTag.addField(createArtworkFromFile(file))
    }

    boolean hasArtwork() {
        wrappedTag.getFirstArtwork()
    }

    Version getVersion() {
        version
    }

}
