package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.FULL_DATE
import static org.venutolo.mp3.core.Field.YEAR

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractMp3FileFix

@Slf4j
class YearFix extends AbstractMp3FileFix {

    YearFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    boolean fixInternal(@Nonnull final Mp3File mp3File) {
        def fixed = false
        def tag = mp3File.getID3v2Tag()
        def yearValue = tag.get(YEAR)
        if (yearValue && FULL_DATE.matcher(yearValue).matches()) {
            log.debug("Truncating {} field: {}", YEAR, yearValue)
            tag.set(YEAR, yearValue.take(4))
            output.write(mp3File, "Truncated ${YEAR}")
            fixed = true
        }
        fixed
    }

}
