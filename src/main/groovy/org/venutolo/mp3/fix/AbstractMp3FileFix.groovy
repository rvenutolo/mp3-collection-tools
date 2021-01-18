package org.venutolo.mp3.fix

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.output.Output

abstract class AbstractMp3FileFix implements Mp3FileFix {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMp3FileFix(@Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags) {
        this.log = requireNonNull(log, 'Logger cannot be null')
        this.output = requireNonNull(output, 'Output cannot be null')
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    boolean fix(@Nonnull final MP3File mp3File) {
        log.debug('Fixing: {}', mp3File.file.canonicalPath)
        requireNonNull(mp3File, 'MP3 file cannot be null')
        def fixed = (!requiresId3v2Tags || (requiresId3v2Tags && mp3File.hasID3v2Tag())) ? fixInternal(mp3File) : false
        if (fixed) {
            log.debug('Fixed: {}', mp3File.file.canonicalPath)
        } else {
            log.debug('No fix applied: {}', mp3File.file.canonicalPath)
        }
        fixed
    }

    protected abstract boolean fixInternal(@Nonnull final MP3File mp3File)

}
