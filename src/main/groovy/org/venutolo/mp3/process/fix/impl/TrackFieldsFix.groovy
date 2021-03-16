package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Field.TRACK
import static org.venutolo.mp3.core.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import java.util.regex.Pattern
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractMp3FileFix

@Slf4j
class TrackFieldsFix extends AbstractMp3FileFix {

    private static final Pattern ZERO_PADDING = ~$/^0+(?!$)/$

    TrackFieldsFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final Mp3File mp3File) {
        def tag = mp3File.getId3v2Tag()
        def fixed = false
        [TRACK, TRACK_TOTAL].each { field ->
            def val = tag.get(field)
            if (val.startsWith('0')) {
                def newVal = val.replaceFirst(ZERO_PADDING, '')
                tag.set(field, newVal)
                output.write(mp3File, "Removed ${field} 0-padding")
                fixed = true
            }
        }
        fixed
    }

}
