package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Constants.EXTRANEOUS_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.fix.AbstractMp3FileFix

@Slf4j
class ExtraneousFieldFix extends AbstractMp3FileFix {

    ExtraneousFieldFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    boolean fixInternal(@Nonnull final MP3File mp3File) {
        def fixed = false
        def tag = mp3File.getID3v2Tag()
        EXTRANEOUS_FIELDS.each { field ->
            def fieldValue = tag.getFirst(field.key)
            if (fieldValue) {
                log.debug('Removing {} field: {}', field.desc, fieldValue)
                tag.deleteField(field.key)
                output.write(mp3File, "Removed: ${field.desc}")
                fixed = true
            }
        }
        fixed
    }

}
