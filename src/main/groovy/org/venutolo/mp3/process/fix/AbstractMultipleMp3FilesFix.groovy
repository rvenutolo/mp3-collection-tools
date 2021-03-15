package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull
import org.slf4j.Logger
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.traits.LogAndOutputValidation
import org.venutolo.mp3.process.traits.MultipleMp3FileProcess

abstract class AbstractMultipleMp3FilesFix
    implements MultipleMp3FilesFix, MultipleMp3FileProcess, LogAndOutputValidation {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMultipleMp3FilesFix(
        @Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags
    ) {
        validateLogAndOutput(log, output)
        this.log = log
        this.output = output
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    boolean fix(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir) {
        validateMp3FilesAndDir(mp3Files, dir)
        def fixed = false
        if (shouldRunProcess(mp3Files, requiresId3v2Tags)) {
            log.debug('Fixing MP3 files in: {}', dir.canonicalPath)
            fixed = fixInternal(mp3Files, dir)
            if (fixed) {
                log.debug('Fixed MP3 files in: {}', dir.canonicalPath)
            } else {
                log.debug('No fix applied to MP3 files in: {}', dir.canonicalPath)
            }
        } else {
            log.debug('Skipping MP3 files in: {}', dir.canonicalPath)
        }
        fixed
    }

    protected abstract boolean fixInternal(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir)

}
