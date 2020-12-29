package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.EXTRANEOUS_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class ExtraneousFieldsCheck extends AbstractMp3FileCheck {

    ExtraneousFieldsCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2TagAsv24()
        EXTRANEOUS_FIELDS.each { field ->
            def fieldValues = tag.getAll(field.key)
            if (fieldValues) {
                warningOutput.write(mp3File, "Extraneous field: ${field.desc}")
            }
        }
    }

}
