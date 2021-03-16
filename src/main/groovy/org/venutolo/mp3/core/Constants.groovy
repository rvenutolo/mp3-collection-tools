package org.venutolo.mp3.core

import static Field.ALBUM
import static Field.ALBUM_ARTIST
import static Field.GENRE
import static Field.TRACK_TOTAL
import static Field.YEAR
import static org.venutolo.mp3.core.Id3v2Tag.Version.V2_4

import java.util.regex.Pattern
import static org.venutolo.mp3.core.Id3v2Tag.Version

class Constants {

    public static final Version ID3V2_TARGET_VERSION = V2_4
    public static final String ALBUM_IMAGE_FILENAME = 'Folder.jpg'
    public static final String ALBUM_IMAGE_FORMAT = 'JPEG'
    public static final int TARGET_PIXELS = 1000
    public static final Pattern FOUR_DIGITS = ~$/\d{4}/$
    public static final Pattern FULL_DATE = ~$/\d{4}-\d{2}-\d{2}/$
    public static final Collection<Field> REQUIRED_FIELDS = Field.values().findAll { field -> field.isRequired }
    public static final Collection<Field> EXTRANEOUS_FIELDS = Field.values().findAll { field -> !field.isRequired }
    public static final Collection<Field> NUMERIC_FIELDS = Field.values().findAll { field -> field.isNumeric }
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
