package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Constants.FULL_DATE
import static org.venutolo.mp3.Field.YEAR

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.fix.AbstractMp3FileFix

@Slf4j
class YearFix extends AbstractMp3FileFix {

    YearFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    boolean fixInternal(@Nonnull final MP3File mp3File) {
        def fixed = false
        def tag = mp3File.getID3v2Tag()
        def yearValue = tag.getFirst(YEAR.key)
        if (yearValue && FULL_DATE.matcher(yearValue).matches()) {
            log.debug("Truncating {} field: {}", YEAR.desc, yearValue)
            tag.setField(YEAR.key, yearValue.take(4))
            output.write(mp3File, "Truncated ${YEAR.desc}")
            fixed = true
        }
        fixed
    }

}
