package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull
import static org.jaudiotagger.tag.FieldKey.COVER_ART
import static org.jaudiotagger.tag.id3.valuepair.ImageFormats.getMimeTypeForBinarySignature
import static org.jaudiotagger.tag.images.ArtworkFactory.createArtworkFromFile
import static org.jaudiotagger.tag.reference.PictureTypes.DEFAULT_ID
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_2
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_3
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import javax.imageio.ImageIO
import org.jaudiotagger.tag.id3.AbstractID3v2Tag as JatAbstractId3v2Tag
import org.jaudiotagger.tag.id3.ID3v22Tag as JatId3v22Tag
import org.jaudiotagger.tag.id3.ID3v23Tag as JatId3v23Tag
import org.jaudiotagger.tag.id3.ID3v24Tag as JatId3v24Tag
import org.jaudiotagger.tag.images.ArtworkFactory
import org.venutolo.mp3.core.Id3v2Tag

final class JAudioTaggerId3v2Tag extends AbstractJAudioTaggerId3Tag<JatAbstractId3v2Tag> implements Id3v2Tag {

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
    @Nonnull
    Version getVersion() {
        version
    }

    @Override
    boolean hasArtwork() {
        jatTag.getFirstArtwork()
    }

    @Override
    void setArtwork(@Nonnull final File file) {
        requireNonNull(file, 'Artwork file cannot be null')
        jatTag.addField(createArtworkFromFile(file))
    }

    @Override
    void setArtwork(@Nonnull final BufferedImage image) {
        requireNonNull(image, 'Artwork image cannot be null')
        def baos = new ByteArrayOutputStream()
        ImageIO.write(image, "png", baos)
        def bytes = baos.toByteArray()
        def artwork = ArtworkFactory.getNew().tap {
            setBinaryData(bytes)
            setMimeType(getMimeTypeForBinarySignature(bytes))
            setDescription('')
            setPictureType(DEFAULT_ID)
        }
        jatTag.addField(artwork)
    }

    @Override
    @Nullable
    BufferedImage getArtwork() {
        jatTag.getFirstArtwork()?.getImage() as BufferedImage
    }

    @Override
    void deleteArtwork() {
        jatTag.deleteField(COVER_ART)
    }

    @Override
    Id3v2Tag asVersion(@Nonnull final Version version) {
        requireNonNull(version, 'Version cannot be null')
        def newTag = new JAudioTaggerId3v2Tag(VERSION_TO_CLASS_MAP[version].newInstance() as JatAbstractId3v2Tag)
        populatedFields().each { field, value -> newTag.set(field, value) }
        if (hasArtwork()) {
            newTag.setArtwork(getArtwork())
        }
        newTag
    }

    @Override
    boolean equals(o) {
        if (this.is(o)) {
            return true
        }
        if (o == null || getClass() != o.class) {
            return false
        }
        def that = (JAudioTaggerId3v2Tag) o
        if (version != that.version) {
            return false
        }
        if (jatTag != that.jatTag) {
            return false
        }
        return true
    }

    @Override
    int hashCode() {
        // Wrapped class does not implement hashCode().
        // AbstractID3v2Tag::equals uses frameMap, so use that in hash.
        Objects.hash(jatTag.frameMap, version)
    }

}
