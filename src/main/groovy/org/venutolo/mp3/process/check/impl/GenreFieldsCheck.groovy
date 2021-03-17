package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.ALLOWED_GENRES
import static org.venutolo.mp3.core.Field.GENRE

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
class GenreFieldsCheck extends AbstractMp3FileCheck {

    GenreFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Mp3File mp3File) {
        def genre = mp3File.getId3v2Tag().get(GENRE)
        if (genre && !ALLOWED_GENRES.contains(genre)) {
            output.write(mp3File, "Unexpected ${GENRE}", genre)
        }
    }

}
