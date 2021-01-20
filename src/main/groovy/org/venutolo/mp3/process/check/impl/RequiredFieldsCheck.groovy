package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Constants.REQUIRED_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
class RequiredFieldsCheck extends AbstractMp3FileCheck {

    RequiredFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2Tag()
        REQUIRED_FIELDS.each { field ->
            def fieldValues = tag.getAll(field.key)
            if (!fieldValues) {
                output.write(mp3File, "Missing field: ${field.desc}")
            } else if (fieldValues.size() > 1) {
                output.write(mp3File, "Multiple values for field: ${field.desc}", fieldValues.join(', '))
            }
        }
    }

}
