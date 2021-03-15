package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Field.TRACK

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMultipleMp3FilesCheck

@Slf4j
class OverlappingTrackCheck extends AbstractMultipleMp3FilesCheck {

    OverlappingTrackCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir) {
        mp3Files
            .countBy { mp3File -> mp3File.getID3v2Tag().get(TRACK) }
            .findAll { trackNum, count -> trackNum && count > 1 }
            .sort()
            .each { trackNum, count -> output.write(dir, "Multiple ${TRACK} #${trackNum}") }
    }

}
