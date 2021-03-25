package org.venutolo.mp3.core

import static org.venutolo.mp3.core.Constants.FOUR_DIGITS
import static org.venutolo.mp3.core.Constants.POSITIVE_INTEGER

import java.util.regex.Pattern
import javax.annotation.Nonnull
import javax.annotation.Nullable

enum Field {

    ACOUSTID_FINGERPRINT('AcoustID fingerprint', false, false),
    ACOUSTID_ID('AcoustID ID', false, false),
    ALBUM('album', true, true),
    ALBUM_ARTIST('album artist', true, false),
    ALBUM_ARTIST_SORT('album artist sort', false, false),
    ALBUM_SORT('album sort', false, false),
    AMAZON_ID('Amazon ID', false, false),
    ARRANGER('arranger', false, false),
    ARTIST('artist', true, true),
    ARTIST_SORT('artist sort', false, false),
    ARTISTS('artists', false, false),
    BARCODE('barcode', false, false),
    BPM('BPM', false, false),
    CATALOG_NO('catalog number', false, false),
    COMMENT('comment', false, true),
    COMPOSER('composer', false, false),
    COMPOSER_SORT('composer sort', false, false),
    CONDUCTOR('conductor', false, false),
    COUNTRY('country', false, false),
    DISC_NO('disc number', false, false, POSITIVE_INTEGER),
    DISC_SUBTITLE('disc subtitle', false, false),
    DISC_TOTAL('discs total', false, false, POSITIVE_INTEGER),
    DJMIXER('DJ mixer', false, false),
    ENCODER('encoder', false, false),
    ENGINEER('engineer', false, false),
    FBPM('FBPM', false, false),
    GENRE('genre', true, true),
    GROUPING('grouping', false, false),
    ISRC('ISRC', false, false),
    IS_COMPILATION('is compilation', false, false),
    KEY('key', false, false),
    LANGUAGE('language', false, false),
    LYRICIST('lyricist', false, false),
    LYRICS('lyrics', false, false),
    MEDIA('media', false, false),
    MIXER('mixer', false, false),
    MOOD('mood', false, false),
    MUSICBRAINZ_ARTISTID('MusicBrainz artist ID', false, false),
    MUSICBRAINZ_DISC_ID('MusicBrainz disc ID', false, false),
    MUSICBRAINZ_ORIGINAL_RELEASE_ID('MusicBrainz original release ID', false, false),
    MUSICBRAINZ_RELEASEARTISTID('MusicBrainz release artist ID', false, false),
    MUSICBRAINZ_RELEASEID('MusicBrainz released', false, false),
    MUSICBRAINZ_RELEASE_COUNTRY('MusicBrainz release country', false, false),
    MUSICBRAINZ_RELEASE_GROUP_ID('MusicBrainz release group ID', false, false),
    MUSICBRAINZ_RELEASE_STATUS('MusicBrainz release status', false, false),
    MUSICBRAINZ_RELEASE_TRACK_ID('MusicBrainz release track ID', false, false),
    MUSICBRAINZ_RELEASE_TYPE('MusicBrainz release type', false, false),
    MUSICBRAINZ_TRACK_ID('MusicBrainz track ID', false, false),
    MUSICBRAINZ_WORK_ID('MusicBrainz work ID', false, false),
    MUSICIP_ID('MusicIP ID', false, false),
    ORIGINAL_ALBUM('original album', false, false),
    ORIGINAL_ARTIST('original artist', false, false),
    ORIGINAL_LYRICIST('original lyricist', false, false),
    ORIGINAL_YEAR('original year', false, false, FOUR_DIGITS),
    PRODUCER('producer', false, false),
    RATING('rating', false, false, POSITIVE_INTEGER),
    RECORD_LABEL('record label', false, false),
    REMIXER('remixer', false, false),
    SCRIPT('script', false, false),
    SUBTITLE('subtitle', false, false),
    TAGS('tags', false, false),
    TITLE('title', true, true),
    TITLE_SORT('title sort', false, false),
    TRACK('track', true, true, POSITIVE_INTEGER),
    TRACK_TOTAL('track total', true, false, POSITIVE_INTEGER),
    URL_DISCOGS_ARTIST_SITE('Discogs artist site', false, false),
    URL_DISCOGS_RELEASE_SITE('Discogs release site', false, false),
    URL_LYRICS_SITE('lyrics site', false, false),
    URL_OFFICIAL_ARTIST_SITE('official artist site', false, false),
    URL_OFFICIAL_RELEASE_SITE('official release site', false, false),
    URL_WIKIPEDIA_ARTIST_SITE('Wikipedia artist site', false, false),
    URL_WIKIPEDIA_RELEASE_SITE('Wikipedia release site', false, false),
    YEAR('year', true, true, FOUR_DIGITS)

    @Nonnull private final String desc
    final boolean isRequired
    final boolean isId3
    @Nullable final Pattern pattern

    private Field(
        @Nonnull final String desc,
        final boolean isRequired,
        final boolean isId3,
        final Pattern pattern = null
    ) {
        this.desc = desc
        this.isRequired = isRequired
        this.isId3 = isId3
        this.pattern = pattern
    }

    String toString() {
        desc
    }

}
