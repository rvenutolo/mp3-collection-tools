package org.venutolo.mp3.check

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File

interface Mp3FileCheck {

    void check(@Nonnull final MP3File mp3File)

}