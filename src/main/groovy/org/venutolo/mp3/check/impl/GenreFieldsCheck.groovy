package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.ALLOWED_GENRES
import static org.venutolo.mp3.fields.Field.GENRE

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.check.AbstractMp3FileCheck
import org.venutolo.mp3.output.Output

@Slf4j
class GenreFieldsCheck extends AbstractMp3FileCheck {

    GenreFieldsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def genre = mp3File.getID3v2Tag().getFirst(GENRE.key)
        if (genre && !ALLOWED_GENRES.contains(genre)) {
            output.write(mp3File, "Unexpected ${GENRE.desc}", genre)
        }
    }

}
