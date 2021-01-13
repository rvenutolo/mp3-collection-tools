package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.FOUR_DIGITS
import static org.venutolo.mp3.fields.Field.YEAR

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.check.AbstractMp3FileCheck
import org.venutolo.mp3.output.Output

@Slf4j
class YearFieldCheck extends AbstractMp3FileCheck {

    YearFieldCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2TagAsv24()
        def year = tag.getFirst(YEAR.key)
        if (year && !FOUR_DIGITS.matcher(year).matches()) {
            output.write(mp3File, "${YEAR.desc.capitalize()} not in #### format", year)
        }
    }

}
