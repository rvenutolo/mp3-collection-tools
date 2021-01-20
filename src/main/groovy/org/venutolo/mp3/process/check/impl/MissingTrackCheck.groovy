package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Field.TRACK

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractMultipleMp3FilesCheck

@Slf4j
class MissingTrackCheck extends AbstractMultipleMp3FilesCheck {

    MissingTrackCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def seenTrackNumbers = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK.key) }
            .findAll { s -> !s.isEmpty() }
            .collect { trackNum -> trackNum as Integer }
            .toSet()
        if (!seenTrackNumbers) {
            return
        }
        def maxTrackNumber = seenTrackNumbers.max()
        (1..maxTrackNumber)
            .findAll { trackNum -> !seenTrackNumbers.contains(trackNum) }
            .each { trackNum -> output.write(dir, "Missing ${TRACK.desc} #${trackNum}") }
    }

}