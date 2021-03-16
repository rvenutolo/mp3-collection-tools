package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.FOUR_DIGITS
import static org.venutolo.mp3.core.Field.YEAR

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
class YearFieldCheck extends AbstractMp3FileCheck {

    YearFieldCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Mp3File mp3File) {
        def tag = mp3File.getId3v2Tag()
        def year = tag.get(YEAR)
        if (year && !FOUR_DIGITS.matcher(year).matches()) {
            output.write(mp3File, "${YEAR.toString().capitalize()} not in #### format", year)
        }
    }

}
