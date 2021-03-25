package org.venutolo.mp3.core

import static org.venutolo.mp3.core.Constants.FOUR_DIGITS

import java.util.regex.Pattern
import javax.annotation.Nonnull
import javax.annotation.Nullable

enum Field {

    ACOUSTID_FINGERPRINT('AcoustID fingerprint', false, false, false),
    ACOUSTID_ID('AcoustID ID', false, false, false),
    ALBUM('album', true, false, true),
    ALBUM_ARTIST('album artist', true, false, false),
    ALBUM_ARTIST_SORT('album artist sort', false, false, false),
    ALBUM_SORT('album sort', false, false, false),
    AMAZON_ID('Amazon ID', false, false, false),
    ARRANGER('arranger', false, false, false),
    ARTIST('artist', true, false, true),
    ARTIST_SORT('artist sort', false, false, false),
    ARTISTS('artists', false, false, false),
    BARCODE('barcode', false, false, false),
    BPM('BPM', false, false, false),
    CATALOG_NO('catalog number', false, false, false),
    COMMENT('comment', false, false, true),
    COMPOSER('composer', false, false, false),
    COMPOSER_SORT('composer sort', false, false, false),
    CONDUCTOR('conductor', false, false, false),
    COUNTRY('country', false, false, false),
    DISC_NO('disc number', false, true, false),
    DISC_SUBTITLE('disc subtitle', false, false, false),
    DISC_TOTAL('discs total', false, true, false),
    DJMIXER('DJ mixer', false, false, false),
    ENCODER('encoder', false, false, false),
    ENGINEER('engineer', false, false, false),
    FBPM('FBPM', false, false, false),
    GENRE('genre', true, false, true),
    GROUPING('grouping', false, false, false),
    ISRC('ISRC', false, false, false),
    IS_COMPILATION('is compilation', false, false, false),
    KEY('key', false, false, false),
    LANGUAGE('language', false, false, false),
    LYRICIST('lyricist', false, false, false),
    LYRICS('lyrics', false, false, false),
    MEDIA('media', false, false, false),
    MIXER('mixer', false, false, false),
    MOOD('mood', false, false, false),
    MUSICBRAINZ_ARTISTID('MusicBrainz artist ID', false, false, false),
    MUSICBRAINZ_DISC_ID('MusicBrainz disc ID', false, false, false),
    MUSICBRAINZ_ORIGINAL_RELEASE_ID('MusicBrainz original release ID', false, false, false),
    MUSICBRAINZ_RELEASEARTISTID('MusicBrainz release artist ID', false, false, false),
    MUSICBRAINZ_RELEASEID('MusicBrainz released', false, false, false),
    MUSICBRAINZ_RELEASE_COUNTRY('MusicBrainz release country', false, false, false),
    MUSICBRAINZ_RELEASE_GROUP_ID('MusicBrainz release group ID', false, false, false),
    MUSICBRAINZ_RELEASE_STATUS('MusicBrainz release status', false, false, false),
    MUSICBRAINZ_RELEASE_TRACK_ID('MusicBrainz release track ID', false, false, false),
    MUSICBRAINZ_RELEASE_TYPE('MusicBrainz release type', false, false, false),
    MUSICBRAINZ_TRACK_ID('MusicBrainz track ID', false, false, false),
    MUSICBRAINZ_WORK_ID('MusicBrainz work ID', false, false, false),
    MUSICIP_ID('MusicIP ID', false, false, false),
    ORIGINAL_ALBUM('original album', false, false, false),
    ORIGINAL_ARTIST('original artist', false, false, false),
    ORIGINAL_LYRICIST('original lyricist', false, false, false),
    ORIGINAL_YEAR('original year', false, false, false, FOUR_DIGITS),
    PRODUCER('producer', false, false, false),
    RATING('rating', false, true, false),
    RECORD_LABEL('record label', false, false, false),
    REMIXER('remixer', false, false, false),
    SCRIPT('script', false, false, false),
    SUBTITLE('subtitle', false, false, false),
    TAGS('tags', false, false, false),
    TITLE('title', true, false, true),
    TITLE_SORT('title sort', false, false, false),
    TRACK('track', true, true, true),
    TRACK_TOTAL('track total', true, true, false),
    URL_DISCOGS_ARTIST_SITE('Discogs artist site', false, false, false),
    URL_DISCOGS_RELEASE_SITE('Discogs release site', false, false, false),
    URL_LYRICS_SITE('lyrics site', false, false, false),
    URL_OFFICIAL_ARTIST_SITE('official artist site', false, false, false),
    URL_OFFICIAL_RELEASE_SITE('official release site', false, false, false),
    URL_WIKIPEDIA_ARTIST_SITE('Wikipedia artist site', false, false, false),
    URL_WIKIPEDIA_RELEASE_SITE('Wikipedia release site', false, false, false),
    YEAR('year', true, false, true, FOUR_DIGITS)

    @Nonnull private final String desc
    final boolean isRequired
    final boolean isNumeric
    final boolean isId3
    @Nullable final Pattern pattern

    private Field(
        @Nonnull final String desc,
        final boolean isRequired,
        final boolean isNumeric,
        final boolean isId3,
        final Pattern pattern = null
    ) {
        this.desc = desc
        this.isRequired = isRequired
        this.isNumeric = isNumeric
        this.isId3 = isId3
        this.pattern = pattern
    }

    String toString() {
        desc
    }

}
