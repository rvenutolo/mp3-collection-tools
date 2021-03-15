package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.REQUIRED_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
class RequiredFieldsCheck extends AbstractMp3FileCheck {

    RequiredFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Mp3File mp3File) {
        def tag = mp3File.getID3v2Tag()
        REQUIRED_FIELDS.each { field ->
            def fieldValue = tag.get(field)
            if (!fieldValue) {
                output.write(mp3File, "Missing field: ${field}")
            }
        }
    }

}
