package org.venutolo.mp3.check

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.slf4j.Logger
import org.venutolo.mp3.output.Output

abstract class AbstractMultipleMp3FilesCheck implements MultipleMp3FilesCheck {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresId3v2Tags

    AbstractMultipleMp3FilesCheck(
        @Nonnull final Logger log, @Nonnull final Output output, final boolean requiresId3v2Tags
    ) {
        this.log = requireNonNull(log, 'Logger cannot be null')
        this.output = requireNonNull(output, 'Output cannot be null')
        this.requiresId3v2Tags = requiresId3v2Tags
    }

    @Override
    void check(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        log.debug('Checking MP3 files in: {}', dir.canonicalPath)
        requireNonNull(mp3Files, 'MP3 files cannot be null')
        requireNonNull(dir, 'Directory cannot be null')
        if (mp3Files.isEmpty()) {
            throw new IllegalArgumentException('MP3 files cannot be empty')
        }
        if (dir.isFile()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
        if (requiresId3v2Tags) {
            checkInternal(mp3Files.findAll { mp3File -> mp3File.hasID3v2Tag() }, dir)
        } else {
            checkInternal(mp3Files, dir)
        }
        log.debug('Checked MP3 files in: {}', dir.canonicalPath)
    }

    protected abstract void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir)

}
