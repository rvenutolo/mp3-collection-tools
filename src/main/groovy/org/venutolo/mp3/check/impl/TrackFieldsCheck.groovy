package org.venutolo.mp3.check.impl

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
        [TRACK, TRACK_TOTAL].each { field ->
            def val = tag.getFirst(field.key)
            if (val.startsWith('0')) {
                output.write(mp3File, "${field.desc} has 0-padding", val)
            }
        }
    }

}
