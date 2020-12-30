package org.venutolo.mp3.check

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File

interface MultipleMp3FilesCheck {

    void check(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir)

}