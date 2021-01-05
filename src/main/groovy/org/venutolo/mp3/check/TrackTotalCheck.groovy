package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.Output

@Slf4j
class TrackTotalCheck extends AbstractMultipleMp3FilesCheck {

    TrackTotalCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def maxTrackNumber = mp3Files
            .collect { mp3File -> mp3File.getID3v2TagAsv24().getFirst(TRACK.key) }
            .findAll { s -> !s.isEmpty() }
            .collect { s -> s as Integer }
            .max()
        if (!maxTrackNumber) {
            return
        }
        def anyTrackTotalWrong = mp3Files
            .collect { mp3File -> mp3File.getID3v2TagAsv24().getFirst(TRACK_TOTAL.key) }
            .findAll { s -> !s.isEmpty() }
            .collect { s -> s as Integer }
            .any { trackTotal -> trackTotal != maxTrackNumber }
        if (anyTrackTotalWrong) {
            output.write(dir, 'Wrong total tracks')
        }
    }

}

