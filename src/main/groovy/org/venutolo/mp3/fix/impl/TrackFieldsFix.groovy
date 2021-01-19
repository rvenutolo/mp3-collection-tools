package org.venutolo.mp3.fix.impl

import static org.venutolo.mp3.fields.Field.TRACK
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import java.util.regex.Pattern
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fix.AbstractMp3FileFix
import org.venutolo.mp3.output.Output

@Slf4j
class TrackFieldsFix extends AbstractMp3FileFix {

    private static final Pattern ZERO_PADDING = ~$/^0+(?!$)/$

    TrackFieldsFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2Tag()
        def fixed = false
        [TRACK, TRACK_TOTAL].each { field ->
            def val = tag.getFirst(field.key)
            if (val.startsWith('0')) {
                def newVal = val.replaceFirst(ZERO_PADDING, '')
                tag.setField(field.key, newVal)
                output.write(mp3File, "Removed ${field.desc} 0-padding")
                fixed = true
            }
        }
        fixed
    }

}
