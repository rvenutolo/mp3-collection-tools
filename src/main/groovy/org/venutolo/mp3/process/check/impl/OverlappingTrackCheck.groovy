package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Field.TRACK

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractMultipleMp3FilesCheck

@Slf4j
class OverlappingTrackCheck extends AbstractMultipleMp3FilesCheck {

    OverlappingTrackCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        mp3Files
            .countBy { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK.key) }
            .findAll { trackNum, count -> trackNum && count > 1 }
            .sort()
            .each { trackNum, count -> output.write(dir, "Multiple ${TRACK.desc} #${trackNum}") }
    }

}