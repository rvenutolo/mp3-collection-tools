package org.venutolo.mp3.process.check

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File

interface MultipleMp3FilesCheck {

    void check(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir)

}