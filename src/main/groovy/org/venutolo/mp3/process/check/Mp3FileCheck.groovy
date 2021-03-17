package org.venutolo.mp3.process.check

import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File

interface Mp3FileCheck {

    void check(@Nonnull final Mp3File mp3File)

}