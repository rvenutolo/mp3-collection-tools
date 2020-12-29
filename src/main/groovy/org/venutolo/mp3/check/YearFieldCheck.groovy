package org.venutolo.mp3.check

import groovy.util.logging.Slf4j
import java.util.regex.Pattern
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fields.Field
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class YearFieldCheck extends AbstractMp3FileCheck {

    private static final Pattern FOUR_DIGITS = ~$/\d{4}/$

    YearFieldCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def tag = mp3File.getID3v2TagAsv24()
        def year = tag.getFirst(Field.YEAR.key)
        if (year && !FOUR_DIGITS.matcher(year).matches()) {
            warningOutput.write(mp3File, 'Year not in #### format', year)
        }
    }

}
