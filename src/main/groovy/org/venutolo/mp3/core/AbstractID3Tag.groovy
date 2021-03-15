package org.venutolo.mp3.core

import static java.util.Objects.requireNonNull

import javax.annotation.Nonnull
import org.jaudiotagger.tag.FieldKey as WrappedField
import org.jaudiotagger.tag.Tag as WrappedTag

abstract class AbstractID3Tag<T extends WrappedTag> {

    @Nonnull
    protected abstract T getWrappedTag()

    boolean has(@Nonnull final Field field) {
        get(field)
    }

    @Nonnull
    String get(@Nonnull final Field field) {
        requireNonNull(field, "Field cannot be null")
        def wrappedField = toWrappedField(field)
        // getAll(field) returns an unexpected value for RATING
        def allValues = field == Field.RATING
            ? [getWrappedTag().getFirst(wrappedField)]
            : getWrappedTag().getAll(wrappedField)
        if (allValues.size() > 1) {
            // I think and hope this won't happen, but I'd like to know if it does.
            throw new IllegalStateException("${field} has multiple values: ${allValues.join(', ')}")
        }
        allValues.size() == 1 ? allValues[0] : ''
    }

    void set(@Nonnull final Field field, @Nonnull final String value) {
        requireNonNull(field, "Field cannot be null")
        requireNonNull(value, "Value cannot be null")
        def wrappedField = toWrappedField(field)
        getWrappedTag().setField(wrappedField, value)
    }

    void delete(@Nonnull final Field field) {
        requireNonNull(field, "Field cannot be null")
        def wrappedField = toWrappedField(field)
        getWrappedTag().deleteField(wrappedField)
    }

    @Nonnull
    private static WrappedField toWrappedField(@Nonnull final Field field) {
        if (!FIELD_TO_FIELD_KEY_MAP.containsKey(field)) {
            throw new IllegalStateException("No mapping for: ${field}")
        }
        FIELD_TO_FIELD_KEY_MAP[field]
    }

    private static final Map<Field, WrappedField> FIELD_TO_FIELD_KEY_MAP = [
        (Field.ACOUSTID_FINGERPRINT)           : WrappedField.ACOUSTID_FINGERPRINT,
        (Field.ACOUSTID_ID)                    : WrappedField.ACOUSTID_ID,
        (Field.ALBUM)                          : WrappedField.ALBUM,
        (Field.ALBUM_ARTIST)                   : WrappedField.ALBUM_ARTIST,
        (Field.ALBUM_ARTIST_SORT)              : WrappedField.ALBUM_ARTIST_SORT,
        (Field.ALBUM_SORT)                     : WrappedField.ALBUM_SORT,
        (Field.AMAZON_ID)                      : WrappedField.AMAZON_ID,
        (Field.ARRANGER)                       : WrappedField.ARRANGER,
        (Field.ARTIST)                         : WrappedField.ARTIST,
        (Field.ARTIST_SORT)                    : WrappedField.ARTIST_SORT,
        (Field.ARTISTS)                        : WrappedField.ARTISTS,
        (Field.BARCODE)                        : WrappedField.BARCODE,
        (Field.BPM)                            : WrappedField.BPM,
        (Field.CATALOG_NO)                     : WrappedField.CATALOG_NO,
        (Field.COMMENT)                        : WrappedField.COMMENT,
        (Field.COMPOSER)                       : WrappedField.COMPOSER,
        (Field.COMPOSER_SORT)                  : WrappedField.COMPOSER_SORT,
        (Field.CONDUCTOR)                      : WrappedField.CONDUCTOR,
        (Field.COUNTRY)                        : WrappedField.COUNTRY,
        (Field.COVER_ART)                      : WrappedField.COVER_ART,
        (Field.DISC_NO)                        : WrappedField.DISC_NO,
        (Field.DISC_SUBTITLE)                  : WrappedField.DISC_SUBTITLE,
        (Field.DISC_TOTAL)                     : WrappedField.DISC_TOTAL,
        (Field.DJMIXER)                        : WrappedField.DJMIXER,
        (Field.ENCODER)                        : WrappedField.ENCODER,
        (Field.ENGINEER)                       : WrappedField.ENGINEER,
        (Field.FBPM)                           : WrappedField.FBPM,
        (Field.GENRE)                          : WrappedField.GENRE,
        (Field.GROUPING)                       : WrappedField.GROUPING,
        (Field.ISRC)                           : WrappedField.ISRC,
        (Field.IS_COMPILATION)                 : WrappedField.IS_COMPILATION,
        (Field.KEY)                            : WrappedField.KEY,
        (Field.LANGUAGE)                       : WrappedField.LANGUAGE,
        (Field.LYRICIST)                       : WrappedField.LYRICIST,
        (Field.LYRICS)                         : WrappedField.LYRICS,
        (Field.MEDIA)                          : WrappedField.MEDIA,
        (Field.MIXER)                          : WrappedField.MIXER,
        (Field.MOOD)                           : WrappedField.MOOD,
        (Field.MUSICBRAINZ_ARTISTID)           : WrappedField.MUSICBRAINZ_ARTISTID,
        (Field.MUSICBRAINZ_DISC_ID)            : WrappedField.MUSICBRAINZ_DISC_ID,
        (Field.MUSICBRAINZ_ORIGINAL_RELEASE_ID): WrappedField.MUSICBRAINZ_ORIGINAL_RELEASE_ID,
        (Field.MUSICBRAINZ_RELEASEARTISTID)    : WrappedField.MUSICBRAINZ_RELEASEARTISTID,
        (Field.MUSICBRAINZ_RELEASEID)          : WrappedField.MUSICBRAINZ_RELEASEID,
        (Field.MUSICBRAINZ_RELEASE_COUNTRY)    : WrappedField.MUSICBRAINZ_RELEASE_COUNTRY,
        (Field.MUSICBRAINZ_RELEASE_GROUP_ID)   : WrappedField.MUSICBRAINZ_RELEASE_GROUP_ID,
        (Field.MUSICBRAINZ_RELEASE_STATUS)     : WrappedField.MUSICBRAINZ_RELEASE_STATUS,
        (Field.MUSICBRAINZ_RELEASE_TRACK_ID)   : WrappedField.MUSICBRAINZ_RELEASE_TRACK_ID,
        (Field.MUSICBRAINZ_RELEASE_TYPE)       : WrappedField.MUSICBRAINZ_RELEASE_TYPE,
        (Field.MUSICBRAINZ_TRACK_ID)           : WrappedField.MUSICBRAINZ_TRACK_ID,
        (Field.MUSICBRAINZ_WORK_ID)            : WrappedField.MUSICBRAINZ_WORK_ID,
        (Field.MUSICIP_ID)                     : WrappedField.MUSICIP_ID,
        (Field.ORIGINAL_ALBUM)                 : WrappedField.ORIGINAL_ALBUM,
        (Field.ORIGINAL_ARTIST)                : WrappedField.ORIGINAL_ARTIST,
        (Field.ORIGINAL_LYRICIST)              : WrappedField.ORIGINAL_LYRICIST,
        (Field.ORIGINAL_YEAR)                  : WrappedField.ORIGINAL_YEAR,
        (Field.PRODUCER)                       : WrappedField.PRODUCER,
        (Field.RATING)                         : WrappedField.RATING,
        (Field.RECORD_LABEL)                   : WrappedField.RECORD_LABEL,
        (Field.REMIXER)                        : WrappedField.REMIXER,
        (Field.SCRIPT)                         : WrappedField.SCRIPT,
        (Field.SUBTITLE)                       : WrappedField.SUBTITLE,
        (Field.TAGS)                           : WrappedField.TAGS,
        (Field.TITLE)                          : WrappedField.TITLE,
        (Field.TITLE_SORT)                     : WrappedField.TITLE_SORT,
        (Field.TRACK)                          : WrappedField.TRACK,
        (Field.TRACK_TOTAL)                    : WrappedField.TRACK_TOTAL,
        (Field.URL_DISCOGS_ARTIST_SITE)        : WrappedField.URL_DISCOGS_ARTIST_SITE,
        (Field.URL_DISCOGS_RELEASE_SITE)       : WrappedField.URL_DISCOGS_RELEASE_SITE,
        (Field.URL_LYRICS_SITE)                : WrappedField.URL_LYRICS_SITE,
        (Field.URL_OFFICIAL_ARTIST_SITE)       : WrappedField.URL_OFFICIAL_ARTIST_SITE,
        (Field.URL_OFFICIAL_RELEASE_SITE)      : WrappedField.URL_OFFICIAL_RELEASE_SITE,
        (Field.URL_WIKIPEDIA_ARTIST_SITE)      : WrappedField.URL_WIKIPEDIA_ARTIST_SITE,
        (Field.URL_WIKIPEDIA_RELEASE_SITE)     : WrappedField.URL_WIKIPEDIA_RELEASE_SITE,
        (Field.YEAR)                           : WrappedField.YEAR
    ].asImmutable()

}
