package org.venutolo.mp3.core.impl.jaudiotagger

import static java.util.Objects.requireNonNull

import java.util.regex.Pattern
import javax.annotation.Nonnull
import org.jaudiotagger.tag.FieldKey as JatField
import org.jaudiotagger.tag.Tag as JatTag
import org.venutolo.mp3.core.Field
import org.venutolo.mp3.core.Id3Tag

abstract class AbstractJAudioTaggerId3Tag<T extends JatTag> implements Id3Tag {

    private static final Pattern INTEGER_REGEX = ~/\d+/

    @Nonnull
    private static JatField toJatField(@Nonnull final Field field) {
        if (!FIELD_TO_FIELD_KEY_MAP.containsKey(field)) {
            throw new IllegalStateException("No mapping for: ${field}")
        }
        FIELD_TO_FIELD_KEY_MAP[field]
    }

    private static final Map<Field, JatField> FIELD_TO_FIELD_KEY_MAP = [
        (Field.ACOUSTID_FINGERPRINT)           : JatField.ACOUSTID_FINGERPRINT,
        (Field.ACOUSTID_ID)                    : JatField.ACOUSTID_ID,
        (Field.ALBUM)                          : JatField.ALBUM,
        (Field.ALBUM_ARTIST)                   : JatField.ALBUM_ARTIST,
        (Field.ALBUM_ARTIST_SORT)              : JatField.ALBUM_ARTIST_SORT,
        (Field.ALBUM_SORT)                     : JatField.ALBUM_SORT,
        (Field.AMAZON_ID)                      : JatField.AMAZON_ID,
        (Field.ARRANGER)                       : JatField.ARRANGER,
        (Field.ARTIST)                         : JatField.ARTIST,
        (Field.ARTIST_SORT)                    : JatField.ARTIST_SORT,
        (Field.ARTISTS)                        : JatField.ARTISTS,
        (Field.BARCODE)                        : JatField.BARCODE,
        (Field.BPM)                            : JatField.BPM,
        (Field.CATALOG_NO)                     : JatField.CATALOG_NO,
        (Field.COMMENT)                        : JatField.COMMENT,
        (Field.COMPOSER)                       : JatField.COMPOSER,
        (Field.COMPOSER_SORT)                  : JatField.COMPOSER_SORT,
        (Field.CONDUCTOR)                      : JatField.CONDUCTOR,
        (Field.COUNTRY)                        : JatField.COUNTRY,
        (Field.DISC_NO)                        : JatField.DISC_NO,
        (Field.DISC_SUBTITLE)                  : JatField.DISC_SUBTITLE,
        (Field.DISC_TOTAL)                     : JatField.DISC_TOTAL,
        (Field.DJMIXER)                        : JatField.DJMIXER,
        (Field.ENCODER)                        : JatField.ENCODER,
        (Field.ENGINEER)                       : JatField.ENGINEER,
        (Field.FBPM)                           : JatField.FBPM,
        (Field.GENRE)                          : JatField.GENRE,
        (Field.GROUPING)                       : JatField.GROUPING,
        (Field.ISRC)                           : JatField.ISRC,
        (Field.IS_COMPILATION)                 : JatField.IS_COMPILATION,
        (Field.KEY)                            : JatField.KEY,
        (Field.LANGUAGE)                       : JatField.LANGUAGE,
        (Field.LYRICIST)                       : JatField.LYRICIST,
        (Field.LYRICS)                         : JatField.LYRICS,
        (Field.MEDIA)                          : JatField.MEDIA,
        (Field.MIXER)                          : JatField.MIXER,
        (Field.MOOD)                           : JatField.MOOD,
        (Field.MUSICBRAINZ_ARTISTID)           : JatField.MUSICBRAINZ_ARTISTID,
        (Field.MUSICBRAINZ_DISC_ID)            : JatField.MUSICBRAINZ_DISC_ID,
        (Field.MUSICBRAINZ_ORIGINAL_RELEASE_ID): JatField.MUSICBRAINZ_ORIGINAL_RELEASE_ID,
        (Field.MUSICBRAINZ_RELEASEARTISTID)    : JatField.MUSICBRAINZ_RELEASEARTISTID,
        (Field.MUSICBRAINZ_RELEASEID)          : JatField.MUSICBRAINZ_RELEASEID,
        (Field.MUSICBRAINZ_RELEASE_COUNTRY)    : JatField.MUSICBRAINZ_RELEASE_COUNTRY,
        (Field.MUSICBRAINZ_RELEASE_GROUP_ID)   : JatField.MUSICBRAINZ_RELEASE_GROUP_ID,
        (Field.MUSICBRAINZ_RELEASE_STATUS)     : JatField.MUSICBRAINZ_RELEASE_STATUS,
        (Field.MUSICBRAINZ_RELEASE_TRACK_ID)   : JatField.MUSICBRAINZ_RELEASE_TRACK_ID,
        (Field.MUSICBRAINZ_RELEASE_TYPE)       : JatField.MUSICBRAINZ_RELEASE_TYPE,
        (Field.MUSICBRAINZ_TRACK_ID)           : JatField.MUSICBRAINZ_TRACK_ID,
        (Field.MUSICBRAINZ_WORK_ID)            : JatField.MUSICBRAINZ_WORK_ID,
        (Field.MUSICIP_ID)                     : JatField.MUSICIP_ID,
        (Field.ORIGINAL_ALBUM)                 : JatField.ORIGINAL_ALBUM,
        (Field.ORIGINAL_ARTIST)                : JatField.ORIGINAL_ARTIST,
        (Field.ORIGINAL_LYRICIST)              : JatField.ORIGINAL_LYRICIST,
        (Field.ORIGINAL_YEAR)                  : JatField.ORIGINAL_YEAR,
        (Field.PRODUCER)                       : JatField.PRODUCER,
        (Field.RATING)                         : JatField.RATING,
        (Field.RECORD_LABEL)                   : JatField.RECORD_LABEL,
        (Field.REMIXER)                        : JatField.REMIXER,
        (Field.SCRIPT)                         : JatField.SCRIPT,
        (Field.SUBTITLE)                       : JatField.SUBTITLE,
        (Field.TAGS)                           : JatField.TAGS,
        (Field.TITLE)                          : JatField.TITLE,
        (Field.TITLE_SORT)                     : JatField.TITLE_SORT,
        (Field.TRACK)                          : JatField.TRACK,
        (Field.TRACK_TOTAL)                    : JatField.TRACK_TOTAL,
        (Field.URL_DISCOGS_ARTIST_SITE)        : JatField.URL_DISCOGS_ARTIST_SITE,
        (Field.URL_DISCOGS_RELEASE_SITE)       : JatField.URL_DISCOGS_RELEASE_SITE,
        (Field.URL_LYRICS_SITE)                : JatField.URL_LYRICS_SITE,
        (Field.URL_OFFICIAL_ARTIST_SITE)       : JatField.URL_OFFICIAL_ARTIST_SITE,
        (Field.URL_OFFICIAL_RELEASE_SITE)      : JatField.URL_OFFICIAL_RELEASE_SITE,
        (Field.URL_WIKIPEDIA_ARTIST_SITE)      : JatField.URL_WIKIPEDIA_ARTIST_SITE,
        (Field.URL_WIKIPEDIA_RELEASE_SITE)     : JatField.URL_WIKIPEDIA_RELEASE_SITE,
        (Field.YEAR)                           : JatField.YEAR
    ].asImmutable()

    @Nonnull
    protected abstract T getJatTag()

    @Override
    @Nonnull
    String get(@Nonnull final Field field) {
        requireNonNull(field, 'Field cannot be null')
        def jatField = toJatField(field)
        // This "all" values is just a check for a situation I don't think will
        // happen, but I'd like to know if it does.
        def allValues = getJatTag().getAll(jatField)
        if (allValues.size() > 1) {
            throw new IllegalStateException("${field} has multiple values: ${allValues.join(', ')}")
        }
        // Return the result of getFirst as in at least one case (RATING), the
        // result from the first element of "all" values isn't what I want.
        getJatTag().getFirst(jatField)
    }

    @Override
    void set(@Nonnull final Field field, @Nonnull final String value) {
        requireNonNull(field, 'Field cannot be null')
        requireNonNull(value, 'Value cannot be null')
        if (value.isEmpty()) {
            throw new IllegalArgumentException("${field} is empty string")
        }
        if (field.isNumeric && !value.isEmpty() && !(value ==~ INTEGER_REGEX)) {
            throw new IllegalArgumentException("Cannot set ${field} to non-numeric value: '${value}'")
        }
        if (field.pattern && !field.pattern.matcher(value).matches()) {
            throw new IllegalArgumentException(
                "Cannot set ${field} to value that does not match expected pattern: '${value}'"
            )
        }
        def jatField = toJatField(field)
        getJatTag().setField(jatField, value)
    }

    @Override
    void delete(@Nonnull final Field field) {
        requireNonNull(field, 'Field cannot be null')
        def jatField = toJatField(field)
        getJatTag().deleteField(jatField)
    }

}
