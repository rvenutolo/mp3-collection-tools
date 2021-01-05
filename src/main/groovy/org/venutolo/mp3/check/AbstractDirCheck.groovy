package org.venutolo.mp3.check

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.slf4j.Logger
import org.venutolo.mp3.output.Output

abstract class AbstractDirCheck implements DirCheck {

    @Nonnull
    private final Logger log

    @Nonnull
    protected final Output output

    private final boolean requiresMp3Files

    AbstractDirCheck(@Nonnull final Logger log, @Nonnull final Output output, final boolean requiresMp3Files) {
        this.log = requireNonNull(log, 'Logger cannot be null')
        this.output = requireNonNull(output, 'Output cannot be null')
        this.requiresMp3Files = requiresMp3Files
    }

    @Override
    void check(@Nonnull final File dir) {
        log.debug('Checking dir: {}', dir.canonicalPath)
        requireNonNull(dir, 'Directory cannot be null')
        if (dir.isFile()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
        def hasMp3s = dir.listFiles().any { file -> file.name.toLowerCase().endsWith('.mp3') }
        if (!requiresMp3Files || (requiresMp3Files && hasMp3s)) {
            checkInternal(dir)
        }
        log.debug('Checked MP3 files in: {}', dir.canonicalPath)
    }

    protected abstract void checkInternal(@Nonnull final File dir)

}
