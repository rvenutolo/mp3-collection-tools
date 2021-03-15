package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File

interface Mp3FileFix {

    boolean fix(@Nonnull final Mp3File mp3File)

}