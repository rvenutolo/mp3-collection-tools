package org.venutolo.mp3.check

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.slf4j.Logger
import org.venutolo.mp3.output.WarningOutput

abstract class AbstractDirCheck implements DirCheck {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final WarningOutput warningOutput

    AbstractDirCheck(@Nonnull final Logger log, @Nonnull final WarningOutput warningOutput) {
        this.log = requireNonNull(log, 'Logger cannot be null')
        this.warningOutput = requireNonNull(warningOutput, 'Warning output cannot be null')
    }

    @Override
    void check(@Nonnull final File dir) {
        log.debug('Checking dir: {}', dir.canonicalPath)
        requireNonNull(dir, 'Directory cannot be null')
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
        checkInternal(dir)
        log.debug('Checked MP3 files in: {}', dir.canonicalPath)
    }

    protected abstract void checkInternal(@Nonnull final File dir)

}
