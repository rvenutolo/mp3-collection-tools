package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.EXTRANEOUS_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractMp3FileFix

@Slf4j
class ExtraneousFieldFix extends AbstractMp3FileFix {

    ExtraneousFieldFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    boolean fixInternal(@Nonnull final Mp3File mp3File) {
        def fixed = false
        def tag = mp3File.getID3v2Tag()
        EXTRANEOUS_FIELDS.each { field ->
            def fieldValue = tag.get(field)
            if (fieldValue) {
                log.debug('Removing {} field: {}', field, fieldValue)
                tag.delete(field)
                output.write(mp3File, "Removed: ${field}")
                fixed = true
            }
        }
        fixed
    }

}
