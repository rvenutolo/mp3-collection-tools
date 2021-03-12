package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull
import org.slf4j.Logger
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.traits.DirProcess
import org.venutolo.mp3.process.traits.LogAndOutputValidation

abstract class AbstractDirFix implements DirFix, DirProcess, LogAndOutputValidation {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresMp3Files

    AbstractDirFix(@Nonnull final Logger log, @Nonnull final Output output, final boolean requiresMp3Files) {
        validateLogAndOutput(log, output)
        this.log = log
        this.output = output
        this.requiresMp3Files = requiresMp3Files
    }

    @Override
    boolean fix(@Nonnull final File dir) {
        validateDir(dir)
        def fixed = false
        if (shouldRunProcess(dir, requiresMp3Files)) {
            log.debug('Fixing dir: {}', dir.canonicalPath)
            fixed = fixInternal(dir)
            if (fixed) {
                log.debug('Fixed dir: {}', dir.canonicalPath)
            } else {
                log.debug('No fix applied to dir: {}', dir.canonicalPath)
            }
        } else {
            log.debug('Skipping dir: {}', dir.canonicalPath)
        }
        fixed
    }

    // Return true when mp3 tags should be written
    protected abstract boolean fixInternal(@Nonnull final File dir)

}
