package org.venutolo.mp3.fix

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File

interface MultipleMp3FilesFix {

    boolean fix(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir)

}