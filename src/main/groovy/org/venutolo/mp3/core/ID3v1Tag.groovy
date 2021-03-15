package org.venutolo.mp3.core

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.tag.id3.ID3v1Tag as WrappedID3v1Tag

// TODO unit test
class ID3v1Tag extends AbstractID3Tag<WrappedID3v1Tag> {

    @Nonnull private final WrappedID3v1Tag wrappedTag

    ID3v1Tag() {
        this(new WrappedID3v1Tag())
    }

    protected ID3v1Tag(@Nonnull private final WrappedID3v1Tag wrappedTag) {
        requireNonNull(wrappedTag, 'Wrapped tag cannot be null')
        this.wrappedTag = wrappedTag
    }

    @Override
    @Nonnull
    protected WrappedID3v1Tag getWrappedTag() {
        wrappedTag
    }

}