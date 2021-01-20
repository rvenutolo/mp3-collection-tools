package org.venutolo.mp3.process.check

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.traits.LogAndOutputValidation
import org.venutolo.mp3.process.traits.MultipleMp3FileProcess

abstract class AbstractMultipleMp3FilesCheck
    implements MultipleMp3FilesCheck, MultipleMp3FileProcess, LogAndOutputValidation {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMultipleMp3FilesCheck(
        @Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags
    ) {
        validateLogAndOutput(log, output)
        this.log = log
        this.output = output
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    void check(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        validateMp3FilesAndDir(mp3Files, dir)
        if (shouldRunProcess(mp3Files, requiresId3v2Tags)) {
            log.debug('Checking MP3 files in: {}', dir.canonicalPath)
            checkInternal(mp3Files, dir)
            log.debug('Checked MP3 files in: {}', dir.canonicalPath)
        } else {
            log.debug('Skipping MP3 files in: {}', dir.canonicalPath)
        }
    }

    protected abstract void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir)

}
