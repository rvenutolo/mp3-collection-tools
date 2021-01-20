package org.venutolo.mp3.process.fix

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.traits.LogAndOutputValidation
import org.venutolo.mp3.process.traits.Mp3FileProcess

abstract class AbstractMp3FileFix implements Mp3FileFix, Mp3FileProcess, LogAndOutputValidation {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMp3FileFix(@Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags) {
        validateLogAndOutput(log, output)
        this.log = log
        this.output = output
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    boolean fix(@Nonnull final MP3File mp3File) {
        validateMp3File(mp3File)
        def fixed = false
        if (shouldRunProcess(mp3File, requiresId3v2Tags)) {
            log.debug('Fixing MP3 file: {}', mp3File.file.canonicalPath)
            fixed = fixInternal(mp3File)
            if (fixed) {
                log.debug('Fixed MP3 file: {}', mp3File.file.canonicalPath)
            } else {
                log.debug('No fix applied to MP3 file: {}', mp3File.file.canonicalPath)
            }
        } else {
            log.debug('Skipping MP3 file: {}', mp3File.file.canonicalPath)
        }
        fixed
    }

    protected abstract boolean fixInternal(@Nonnull final MP3File mp3File)

}
