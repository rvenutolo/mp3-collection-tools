package org.venutolo.mp3.fix

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.output.Output

abstract class AbstractMultipleMp3FilesFix implements MultipleMp3FilesFix {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMultipleMp3FilesFix(
        @Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags
    ) {
        this.log = requireNonNull(log, 'Logger cannot be null')
        this.output = requireNonNull(output, 'Output cannot be null')
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    boolean fix(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        log.debug('Fixing MP3 files in: {}', dir.canonicalPath)
        requireNonNull(mp3Files, 'MP3 files cannot be null')
        requireNonNull(dir, 'Directory cannot be null')
        if (mp3Files.isEmpty()) {
            throw new IllegalArgumentException('MP3 files cannot be empty')
        }
        if (dir.isFile()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
        def fixed = (!requiresId3v2Tags || mp3Files.every { mp3File -> mp3File.hasID3v2Tag() })
            ? fixInternal(mp3Files, dir) : false
        if (fixed) {
            log.debug('Fixed MP3 files in: {}', dir.canonicalPath)
        } else {
            log.debug('No fix applied to MP3 files in: {}', dir.canonicalPath)
        }
        fixed
    }

    protected abstract boolean fixInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir)

}
