package org.venutolo.mp3.check

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.output.WarningOutput

abstract class AbstractMp3FileCheck implements Mp3FileCheck {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final WarningOutput warningOutput

    private final boolean requiresId3v2Tags

    AbstractMp3FileCheck(
        @Nonnull final Logger log, @Nonnull final WarningOutput warningOutput, final boolean requiresId3v2Tags
    ) {
        this.log = requireNonNull(log, 'Logger cannot be null')
        this.warningOutput = requireNonNull(warningOutput, 'Warning output cannot be null')
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    void check(@Nonnull final MP3File mp3File) {
        log.debug('Checking: {}', mp3File.file.canonicalPath)
        requireNonNull(mp3File, 'MP3 file cannot be null')
        if (!requiresId3v2Tags || (requiresId3v2Tags && mp3File.hasID3v2Tag())) {
            checkInternal(mp3File)
        }
        log.debug('Checked: {}', mp3File.file.canonicalPath)
    }

    protected abstract void checkInternal(@Nonnull final MP3File mp3File)

}
