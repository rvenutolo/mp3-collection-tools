package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.GENRE

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class GenreFieldsCheck extends AbstractMp3FileCheck {

    public static final Set<String> ALLOWED_GENRES = [
        'Alternative Metal',
        'Ambient',
        'Audiobook',
        'Blues',
        'Classical',
        'Comedy',
        'Country',
        'Electronic',
        'Funk',
        'Hardcore',
        'Hip-Hop',
        'Jazz',
        'Metal',
        'Metalcore',
        'Noise',
        'Pop',
        'Punk',
        'Reggae',
        'Rock',
        'Ska',
        'Soul',
        'World'
    ]

    GenreFieldsCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final MP3File mp3File) {
        def genre = mp3File.getID3v2TagAsv24().getFirst(GENRE.key)
        if (genre && !ALLOWED_GENRES.contains(genre)) {
            warningOutput.write(mp3File, "Unexpected genre: ${genre}")
        }
    }

}
