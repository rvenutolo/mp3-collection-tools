package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull
import static org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_2
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_3
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import javax.annotation.Nonnull
import org.jaudiotagger.tag.id3.AbstractID3v2Tag as JatAbstractId3v2Tag
import org.jaudiotagger.tag.id3.ID3v22Tag as JatId3v22Tag
import org.jaudiotagger.tag.id3.ID3v23Tag as JatId3v23Tag
import org.jaudiotagger.tag.id3.ID3v24Tag as JatId3v24Tag
import org.venutolo.mp3.core.Id3v2Tag

// TODO unit test
class JAudioTaggerId3v2Tag extends AbstractJAudioTaggerId3Tag<JatAbstractId3v2Tag> implements Id3v2Tag {

    private static final Map<Version, Class<? extends JatAbstractId3v2Tag>> VERSION_TO_CLASS_MAP = [
        (V2_2): JatId3v22Tag,
        (V2_3): JatId3v23Tag,
        (V2_4): JatId3v24Tag,
    ].asImmutable()

    @Nonnull
    private final JatAbstractId3v2Tag jatTag
    @Nonnull
    private final Version version

    JAudioTaggerId3v2Tag(@Nonnull final Version version) {
        requireNonNull(version, 'Version cannot be null')
        this.jatTag = VERSION_TO_CLASS_MAP[version].newInstance()
        this.version = version
    }

    protected JAudioTaggerId3v2Tag(@Nonnull private final JatAbstractId3v2Tag jatTag) {
        requireNonNull(jatTag, 'Tag cannot be null')
        this.jatTag = jatTag
        this.version = VERSION_TO_CLASS_MAP.find { entry -> entry.value == jatTag.class }.key
    }

    @Override
    @Nonnull
    protected JatAbstractId3v2Tag getJatTag() {
        jatTag
    }

    @Override
    void setArtwork(@Nonnull final File file) {
        requireNonNull(file, 'Artwork file cannot be null')
        jatTag.addField(createArtworkFromFile(file))
    }

    @Override
    boolean hasArtwork() {
        jatTag.getFirstArtwork()
    }

    @Override
    @Nonnull
    Version getVersion() {
        version
    }

}
