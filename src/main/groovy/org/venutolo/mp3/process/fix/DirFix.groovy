package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull

interface DirFix {

    boolean fix(@Nonnull final File dir)

}