package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.EXTRANEOUS_FIELDS

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
final class ExtraneousFieldsCheck extends AbstractMp3FileCheck {

    ExtraneousFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Mp3File mp3File) {
        def tag = mp3File.getId3v2Tag()
        EXTRANEOUS_FIELDS
            .findAll { field -> tag.has(field) }
            .each { field -> output.write(mp3File, "Extraneous field: ${field}") }
    }

}
