package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.THREE_DIGITS
import static org.venutolo.mp3.Constants.TWO_DIGITS
import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.Output

@Slf4j
class TrackFieldsCheck extends AbstractMp3FileCheck {

    TrackFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2TagAsv24()
        def track = tag.getFirst(TRACK.key)
        def trackTotal = tag.getFirst(TRACK_TOTAL.key)
        if (track && trackTotal) {
            if (!bothMatchTwoDigitPattern(track, trackTotal) && !bothMatchThreeDigitPattern(track, trackTotal)) {
                output.write(mp3File, 'Track and track total are not in ##/## format', "${track}/${trackTotal}")
            }
        }
    }

    private static boolean bothMatchTwoDigitPattern(@Nonnull final String s1, @Nonnull final String s2) {
        return TWO_DIGITS.matcher(s1).matches() && TWO_DIGITS.matcher(s2).matches()
    }

    private static boolean bothMatchThreeDigitPattern(@Nonnull final String s1, @Nonnull final String s2) {
        return THREE_DIGITS.matcher(s1).matches() && THREE_DIGITS.matcher(s2).matches()
    }

}
