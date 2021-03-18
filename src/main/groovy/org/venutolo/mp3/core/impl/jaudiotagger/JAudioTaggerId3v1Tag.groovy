package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.tag.id3.ID3v1Tag as JatId3v1Tag
import org.venutolo.mp3.core.Id3v1Tag

// TODO unit test
class JAudioTaggerId3v1Tag extends AbstractJAudioTaggerId3Tag<JatId3v1Tag> implements Id3v1Tag {

    @Nonnull private final JatId3v1Tag jatTag

    JAudioTaggerId3v1Tag() {
        this(new JatId3v1Tag())
    }

    protected JAudioTaggerId3v1Tag(@Nonnull private final JatId3v1Tag jatTag) {
        requireNonNull(jatTag, 'Tag cannot be null')
        this.jatTag = jatTag
    }

    @Override
    @Nonnull
    protected JatId3v1Tag getJatTag() {
        jatTag
    }

}