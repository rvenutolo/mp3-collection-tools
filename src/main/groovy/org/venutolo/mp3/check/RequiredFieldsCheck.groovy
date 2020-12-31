package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.REQUIRED_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class RequiredFieldsCheck extends AbstractMp3FileCheck {

    RequiredFieldsCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2TagAsv24()
        REQUIRED_FIELDS.each { field ->
            def fieldValues = tag.getAll(field.key)
            if (!fieldValues) {
                warningOutput.write(mp3File, "Missing field: ${field.desc}")
            } else {
                if (fieldValues.size() > 1) {
                    warningOutput.write(mp3File, "Multiple values for field: ${field.desc}", fieldValues.join(', '))
                }
            }
        }
    }

}
