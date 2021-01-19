package org.venutolo.mp3.fix.impl

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fix.AbstractMp3FileFix
import org.venutolo.mp3.output.Output

@Slf4j
class TrackFieldFix extends AbstractMp3FileFix {

    TrackFieldFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2Tag()
        def track = tag.getFirst(TRACK.key)
        def trackTotal = tag.getFirst(TRACK_TOTAL.key)
        if (!track || !trackTotal) {
            return false
        }
        if (trackTotal.size() == 1) {
            // if track total is only one digit, it should be fixed elsewhere,
            // then this can fix
            return false
        }
        def trackPadded = track.padLeft(trackTotal.length(), '0')
        if (trackPadded == track) {
            return false
        }
        tag.setField(TRACK.key, trackPadded)
        output.write(mp3File, 'Formatted track', trackPadded)
        true
    }

}
