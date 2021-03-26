package org.venutolo.mp3

import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3v2Tag

class TestId3v2Tag implements Id3v2Tag {

    private final Version version
    private Map<Field, String> fieldValues

    TestId3v2Tag(@Nonnull final Version version, @Nonnull final Map<Field, String> fieldValues = [:]) {
        this.version = version
        this.fieldValues = new HashMap<>(fieldValues)
    }

    @Override
    @Nonnull
    String get(@Nonnull final Field field) {
        fieldValues[field] ?: ''
    }

    @Override
    void set(@Nonnull final Field field, @Nonnull final String value) {
        fieldValues[field] = value
    }

    @Override
    void delete(@Nonnull final Field field) {
        fieldValues.remove(field)
    }

    @Override
    @Nonnull
    Version getVersion() {
        version
    }

    @Override
    void setArtwork(@Nonnull final File file) {
        throw new UnsupportedOperationException('not yet implemented')
    }

    @Override
    void setArtwork(@Nonnull final BufferedImage image) {
        throw new UnsupportedOperationException('not yet implemented')
    }

    @Override
    @Nullable
    BufferedImage getArtwork() {
        throw new UnsupportedOperationException('not yet implemented')
    }

    @Override
    boolean hasArtwork() {
        throw new UnsupportedOperationException('not yet implemented')
    }

    @Override
    void deleteArtwork() {
        throw new UnsupportedOperationException('not yet implemented')
    }

    @Override
    @Nonnull
    Id3v2Tag asVersion(@Nonnull final Version version) {
        new TestId3v2Tag(version, this.fieldValues)
    }

}
