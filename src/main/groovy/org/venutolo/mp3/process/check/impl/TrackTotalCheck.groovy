package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Field.TRACK
import static org.venutolo.mp3.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractMultipleMp3FilesCheck

@Slf4j
class TrackTotalCheck extends AbstractMultipleMp3FilesCheck {

    TrackTotalCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def maxTrackNumber = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK.key) }
            .findAll { s -> !s.isEmpty() }
            .collect { s -> s as Integer }
            .max()
        if (!maxTrackNumber) {
            return
        }
        def anyTrackTotalWrong = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) }
            .findAll { s -> !s.isEmpty() }
            .collect { s -> s as Integer }
            .any { trackTotal -> trackTotal != maxTrackNumber }
        if (anyTrackTotalWrong) {
            output.write(dir, "Wrong ${TRACK_TOTAL.desc}")
        }
    }

}

