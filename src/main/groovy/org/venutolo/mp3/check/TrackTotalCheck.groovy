package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class TrackTotalCheck extends AbstractMultipleMp3FilesCheck {

    TrackTotalCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def seenTrackNumbers = mp3Files
            .collect { it.getID3v2TagAsv24().getFirst(TRACK.key) }
            .findAll { it }
            .collect { it as Integer }
            .toSet()
        if (!seenTrackNumbers) {
            return
        }
        def maxTrackNumber = seenTrackNumbers.max()
        def anyTotalTrackWrong = mp3Files.any { mp3File ->
            def trackTotal = mp3File.getID3v2TagAsv24().getFirst(TRACK_TOTAL.key) as Integer
            trackTotal && trackTotal != maxTrackNumber
        }
        if (anyTotalTrackWrong) {
            warningOutput.write(dir, 'Wrong total tracks')
        }
    }

}

