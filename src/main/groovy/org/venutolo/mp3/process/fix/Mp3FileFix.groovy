package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File

interface Mp3FileFix {

    boolean fix(@Nonnull final MP3File mp3File)

}