package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.SAME_VALUE_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fields.Field
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class SameFieldValueCheck extends AbstractMultipleMp3FilesCheck {

    SameFieldValueCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def fieldValues = [:].withDefault { [] as Set<String> } as Map<Field, Set<String>>
        mp3Files.each { mp3File ->
            def tag = mp3File.getID3v2TagAsv24()
            SAME_VALUE_FIELDS.each { field ->
                fieldValues[field] << tag.getFirst(field.key)
            }
        }
        fieldValues.each { field, values ->
            if (values.size() > 1) {
                warningOutput.write(dir, "Non-uniform ${field.desc} values", values.sort().join(', '))
            }
        }
    }

}
