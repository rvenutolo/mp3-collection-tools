package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File

interface MultipleMp3FilesFix {

    boolean fix(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir)

}