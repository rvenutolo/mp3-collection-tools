package org.venutolo.mp3.core

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.tag.id3.ID3v1Tag as WrappedID3v1Tag

// TODO unit test
class ID3v1Tag extends AbstractID3Tag<WrappedID3v1Tag> {

    @Nonnull private final WrappedID3v1Tag wrapped

    ID3v1Tag() {
        this(new WrappedID3v1Tag())
    }

    protected ID3v1Tag(@Nonnull private final WrappedID3v1Tag wrapped) {
        requireNonNull(wrapped, 'Wrapped tag cannot be null')
        this.wrapped = wrapped
    }

    @Override
    @Nonnull
    protected WrappedID3v1Tag getWrappedTag() {
        wrapped
    }

}