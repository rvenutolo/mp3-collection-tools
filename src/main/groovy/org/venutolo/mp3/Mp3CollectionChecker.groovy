package org.venutolo.mp3

import static java.util.Objects.requireNonNull

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.check.DirCheck
import org.venutolo.mp3.check.Mp3FileCheck
import org.venutolo.mp3.check.MultipleMp3FilesCheck

@Slf4j
class Mp3CollectionChecker {

    @Nonnull
    private final Collection<DirCheck> dirChecks
    @Nonnull
    private final Collection<MultipleMp3FilesCheck> mp3FilesChecks
    @Nonnull
    private final Collection<Mp3FileCheck> mp3FileChecks

    Mp3CollectionChecker(
        @Nonnull final Collection<DirCheck> dirChecks,
        @Nonnull final Collection<MultipleMp3FilesCheck> mp3FilesChecks,
        @Nonnull final Collection<Mp3FileCheck> mp3FileChecks
    ) {
        this.dirChecks = requireNonNull(dirChecks, 'Directory checks cannot be null')
        this.mp3FilesChecks = requireNonNull(mp3FilesChecks, 'MP3 files checks cannot be null')
        this.mp3FileChecks = requireNonNull(mp3FileChecks, 'MP3 file checks cannot be null')
    }

    void checkCollection(@Nonnull final File baseDir) {
        log.info("Checking collection at: {}", baseDir.canonicalPath)
        requireNonNull(baseDir, 'Collection base dir cannot be null')
        if (baseDir.isFile()) {
            throw new IllegalArgumentException('Collection base dir is not a directory')
        }
        processDir(baseDir)
        log.info("Checked collection at: {}", baseDir.canonicalPath)
    }

    private void processDir(@Nonnull final File dir) {
        checkDir(dir)
        dir.listFiles().findAll {it.isDirectory()}.sort().each {processDir(it) }
    }

    private void checkDir(@Nonnull final File dir) {
        dirChecks.each { it.check(dir) }
        def mp3Files = dir.listFiles().findAll { it.name.toLowerCase().endsWith('.mp3') }.sort().collect { new MP3File(it) }
        if (mp3Files) {
            mp3FilesChecks.each { it.check(mp3Files, dir) }
            mp3FileChecks.each { mp3FileCheck ->
                mp3Files.each { mp3File ->
                    mp3FileCheck.check(mp3File)
                }
            }
        }
    }

}
