package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Field.TRACK
import static org.venutolo.mp3.core.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
final class TrackFieldsCheck extends AbstractMp3FileCheck {

    TrackFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Mp3File mp3File) {
        def tag = mp3File.getId3v2Tag()
        [TRACK, TRACK_TOTAL].each { field ->
            def val = tag.get(field)
            if (val.startsWith('0')) {
                output.write(mp3File, "${field} has 0-padding", val)
            }
        }
    }

}
