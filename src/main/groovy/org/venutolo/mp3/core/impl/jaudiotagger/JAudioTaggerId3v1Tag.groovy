package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.tag.id3.ID3v11Tag as JatId3v11Tag
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v1Tag

// TODO unit test
final class JAudioTaggerId3v1Tag extends AbstractJAudioTaggerId3Tag<JatId3v11Tag> implements Id3v1Tag {

    @Nonnull private final JatId3v11Tag jatTag

    JAudioTaggerId3v1Tag() {
        this(new JatId3v11Tag())
    }

    protected JAudioTaggerId3v1Tag(@Nonnull private final JatId3v11Tag jatTag) {
        requireNonNull(jatTag, 'Tag cannot be null')
        this.jatTag = jatTag
    }

    @Override
    @Nonnull
    protected JatId3v11Tag getJatTag() {
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
        Objects.hash(
            // TODO get track directly with @track?
            jatTag.track,
            jatTag.album,
            jatTag.artist,
            jatTag.comment,
            jatTag.genre,
            jatTag.title,
            jatTag.year
        )
    }

}