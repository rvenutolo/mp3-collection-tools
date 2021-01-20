package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Field.TRACK
import static org.venutolo.mp3.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
class TrackFieldsCheck extends AbstractMp3FileCheck {

    TrackFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2Tag()
        [TRACK, TRACK_TOTAL].each { field ->
            def val = tag.getFirst(field.key)
            if (val.startsWith('0')) {
                output.write(mp3File, "${field.desc} has 0-padding", val)
            }
        }
    }

}
