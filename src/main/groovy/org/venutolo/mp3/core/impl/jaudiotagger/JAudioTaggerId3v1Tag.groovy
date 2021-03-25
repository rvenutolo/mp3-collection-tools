package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.tag.id3.ID3v1Tag as JatId3v1Tag
import org.jaudiotagger.tag.id3.ID3v11Tag as JatId3v11Tag
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v1Tag

// TODO unit test
final class JAudioTaggerId3v1Tag extends AbstractJAudioTaggerId3Tag<JatId3v1Tag> implements Id3v1Tag {

    @Nonnull private final JatId3v1Tag jatTag

    JAudioTaggerId3v1Tag() {
        this(new JatId3v11Tag())
    }

    protected JAudioTaggerId3v1Tag(@Nonnull private final JatId3v1Tag jatTag) {
        requireNonNull(jatTag, 'Tag cannot be null')
        this.jatTag = (jatTag instanceof JatId3v11Tag) ? jatTag : new JatId3v11Tag(jatTag)
    }

    @Override
    @Nonnull
    protected JatId3v1Tag getJatTag() {
        jatTag
    }

    @Override
    @Nonnull
    String get(@Nonnull final Field field) {
        def fieldValue = super.get(field)
        (field == Field.TRACK && fieldValue == '0') ? '' : fieldValue
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (o == null || getClass() != o.class) {
            return false
        }
        def that = (JAudioTaggerId3v1Tag) o
        if (jatTag != that.jatTag) {
            return false
        }
        return true
    }

    @Override
    int hashCode() {
        Objects.hash(jatTag)
    }

}