package org.venutolo.mp3.process.check

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.traits.LogAndOutputValidation
import org.venutolo.mp3.process.traits.Mp3FileProcess

abstract class AbstractMp3FileCheck implements Mp3FileCheck, Mp3FileProcess, LogAndOutputValidation {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMp3FileCheck(@Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags) {
        validateLogAndOutput(log, output)
        this.log = log
        this.output = output
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    void check(@Nonnull final MP3File mp3File) {
        validateMp3File(mp3File)
        if (shouldRunProcess(mp3File, requiresId3v2Tags)) {
            log.debug('Checking MP3 file: {}', mp3File.file.canonicalPath)
            checkInternal(mp3File)
            log.debug('Checked MP3 file: {}', mp3File.file.canonicalPath)
        } else {
            log.debug('Skipping MP3 file: {}', mp3File.file.canonicalPath)
        }
    }

    protected abstract void checkInternal(@Nonnull final MP3File mp3File)

}
