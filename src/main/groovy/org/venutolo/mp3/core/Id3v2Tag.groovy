package org.venutolo.mp3.core

import javax.annotation.Nonnull

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
        "ID3 v${getVersion()}"
    }

    void setArtwork(@Nonnull final File file)

    boolean hasArtwork()

    @Nonnull
    Version getVersion()

}