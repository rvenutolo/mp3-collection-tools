package org.venutolo.mp3.core

import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable

interface Id3v2Tag extends Id3Tag {

    static enum Version {

        // TODO see where 2.4 is referenced directly instead of through Constants
        V2_2('2.2'),
        V2_3('2.3'),
        V2_4('2.4')

        private final String desc

        private Version(@Nonnull final String desc) {
            this.desc = desc
        }

        String toString() {
            desc
        }
    }

    default String getTagType() {
        "ID3v${getVersion()}"
    }

    @Nonnull
    Version getVersion()

    void setArtwork(@Nonnull final File file)

    void setArtwork(@Nonnull final BufferedImage image)

    @Nullable
    BufferedImage getArtwork()

    boolean hasArtwork()

    void deleteArtwork()

    Id3v2Tag asVersion(@Nonnull final Version version)

}