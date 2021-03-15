package org.venutolo.mp3.core

import javax.annotation.Nonnull

enum Field {

    ACOUSTID_FINGERPRINT('AcoustID fingerprint', false),
    ACOUSTID_ID('AcoustID ID', false),
    ALBUM('album', true),
    ALBUM_ARTIST('album artist', true),
    ALBUM_ARTIST_SORT('album artist sort', false),
    ALBUM_SORT('album sort', false),
    AMAZON_ID('Amazon ID', false),
    ARRANGER('arranger', false),
    ARTIST('artist', true),
    ARTIST_SORT('artist sort', false),
    ARTISTS('artists', false),
    BARCODE('barcode', false),
    BPM('BPM', false),
    CATALOG_NO('catalog number', false),
    COMMENT('comment', false),
    COMPOSER('composer', false),
    COMPOSER_SORT('composer sort', false),
    CONDUCTOR('conductor', false),
    COUNTRY('country', false),
    COVER_ART('cover art', false),
    DISC_NO('disc number', false),
    DISC_SUBTITLE('disc subtitle', false),
    DISC_TOTAL('discs total', false),
    DJMIXER('DJ mixer', false),
    ENCODER('encoder', false),
    ENGINEER('engineer', false),
    FBPM('FBPM', false),
    GENRE('genre', true),
    GROUPING('grouping', false),
    ISRC('ISRC', false),
    IS_COMPILATION('is compilation', false),
    KEY('key', false),
    LANGUAGE('language', false),
    LYRICIST('lyricist', false),
    LYRICS('lyrics', false),
    MEDIA('media', false),
    MIXER('mixer', false),
    MOOD('mood', false),
    MUSICBRAINZ_ARTISTID('MusicBrainz artist ID', false),
    MUSICBRAINZ_DISC_ID('MusicBrainz disc ID', false),
    MUSICBRAINZ_ORIGINAL_RELEASE_ID('MusicBrainz original release ID', false),
    MUSICBRAINZ_RELEASEARTISTID('MusicBrainz release artist ID', false),
    MUSICBRAINZ_RELEASEID('MusicBrainz released', false),
    MUSICBRAINZ_RELEASE_COUNTRY('MusicBrainz release country', false),
    MUSICBRAINZ_RELEASE_GROUP_ID('MusicBrainz release group ID', false),
    MUSICBRAINZ_RELEASE_STATUS('MusicBrainz release status', false),
    MUSICBRAINZ_RELEASE_TRACK_ID('MusicBrainz release track ID', false),
    MUSICBRAINZ_RELEASE_TYPE('MusicBrainz release type', false),
    MUSICBRAINZ_TRACK_ID('MusicBrainz track ID', false),
    MUSICBRAINZ_WORK_ID('MusicBrainz work ID', false),
    MUSICIP_ID('MusicIP ID', false),
    ORIGINAL_ALBUM('original album', false),
    ORIGINAL_ARTIST('original artist', false),
    ORIGINAL_LYRICIST('original lyricist', false),
    ORIGINAL_YEAR('original year', false),
    PRODUCER('producer', false),
    RATING('rating', false),
    RECORD_LABEL('record label', false),
    REMIXER('remixer', false),
    SCRIPT('script', false),
    SUBTITLE('subtitle', false),
    TAGS('tags', false),
    TITLE('title', true),
    TITLE_SORT('title sort', false),
    TRACK('track', true),
    TRACK_TOTAL('track total', true),
    URL_DISCOGS_ARTIST_SITE('Discogs artist site', false),
    URL_DISCOGS_RELEASE_SITE('Discogs release site', false),
    URL_LYRICS_SITE('lyrics site', false),
    URL_OFFICIAL_ARTIST_SITE('official artist site', false),
    URL_OFFICIAL_RELEASE_SITE('official release site', false),
    URL_WIKIPEDIA_ARTIST_SITE('Wikipedia artist site', false),
    URL_WIKIPEDIA_RELEASE_SITE('Wikipedia release site', false),
    YEAR('year', true)

    @Nonnull
    private final String desc
    final boolean required

    private Field(@Nonnull final String desc, final boolean required) {
        this.desc = desc
        this.required = required
    }

    String toString() {
        desc
    }

}
