package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.SAME_VALUE_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMultipleMp3FilesCheck

@Slf4j
class SameFieldValueCheck extends AbstractMultipleMp3FilesCheck {

    SameFieldValueCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir) {
        def fieldValues = [:].withDefault { [] as Set<String> } as Map<Field, Set<String>>
        mp3Files.each { mp3File ->
            def tag = mp3File.getID3v2Tag()
            SAME_VALUE_FIELDS.each { field ->
                fieldValues[field] << tag.get(field)
            }
        }
        fieldValues
            .findAll { field, values -> values.size() > 1 }
            .each { field, values -> output.write(dir, "Non-uniform ${field} values", values.sort().join(', ')) }
    }

}
