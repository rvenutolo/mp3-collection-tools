package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.THREE_DIGITS
import static org.venutolo.mp3.Constants.TWO_DIGITS
import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.check.AbstractMp3FileCheck
import org.venutolo.mp3.output.Output

@Slf4j
class TrackFieldsCheck extends AbstractMp3FileCheck {

    TrackFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2Tag()
        def track = tag.getFirst(TRACK.key)
        def trackTotal = tag.getFirst(TRACK_TOTAL.key)
        if (track && trackTotal) {
            if (!bothMatchTwoDigitPattern(track, trackTotal) && !bothMatchThreeDigitPattern(track, trackTotal)) {
                output.write(
                    mp3File,
                    "${TRACK.desc.capitalize()} and ${TRACK_TOTAL.desc} are not in ##/## format",
                    "${track}/${trackTotal}"
                )
            }
        }
    }

    private static boolean bothMatchTwoDigitPattern(@Nonnull final String s1, @Nonnull final String s2) {
        s1 ==~ TWO_DIGITS && s2 ==~ TWO_DIGITS
    }

    private static boolean bothMatchThreeDigitPattern(@Nonnull final String s1, @Nonnull final String s2) {
        s1 ==~ THREE_DIGITS && s2 ==~ THREE_DIGITS
    }

}
