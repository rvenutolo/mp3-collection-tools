package org.venutolo.mp3

import static org.venutolo.mp3.fields.Field.ALBUM
import static org.venutolo.mp3.fields.Field.ALBUM_ARTIST
import static org.venutolo.mp3.fields.Field.GENRE
import static org.venutolo.mp3.fields.Field.TRACK_TOTAL
import static org.venutolo.mp3.fields.Field.YEAR

import java.util.regex.Pattern
import org.venutolo.mp3.fields.Field

class Constants {

    public static final int ID3V2_TARGET_MAJOR_VERSION = 4
    public static final String ALBUM_IMAGE_FILENAME = 'Folder.jpg'
    public static final int TARGET_PIXELS = 1000
    public static final Pattern FOUR_DIGITS = ~$/\d{4}/$
    public static final Collection<Field> REQUIRED_FIELDS = Field.values().findAll { it.required }
    public static final Collection<Field> EXTRANEOUS_FIELDS = Field.values().findAll { !it.required }
    public static final Collection<Field> SAME_VALUE_FIELDS = [ALBUM_ARTIST, ALBUM, GENRE, YEAR, TRACK_TOTAL]
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

}
