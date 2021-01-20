package org.venutolo.mp3.process.check

import javax.annotation.Nonnull
import org.slf4j.Logger
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.traits.DirProcess
import org.venutolo.mp3.process.traits.LogAndOutputValidation

abstract class AbstractDirCheck implements DirCheck, DirProcess, LogAndOutputValidation {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresMp3Files

    AbstractDirCheck(@Nonnull final Logger log, @Nonnull final Output output, final boolean requiresMp3Files) {
        validateLogAndOutput(log, output)
        this.log = log
        this.output = output
        this.requiresMp3Files = requiresMp3Files
    }

    @Override
    void check(@Nonnull final File dir) {
        validateDir(dir)
        if (shouldRunProcess(dir, requiresMp3Files)) {
            log.debug('Checking dir: {}', dir.canonicalPath)
            checkInternal(dir)
            log.debug('Checked dir: {}', dir.canonicalPath)
        } else {
            log.debug('Skipping dir: {}', dir.canonicalPath)
        }
    }

    protected abstract void checkInternal(@Nonnull final File dir)

}
