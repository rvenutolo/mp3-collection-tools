package org.venutolo.mp3.fields

import javax.annotation.Nonnull
import org.jaudiotagger.tag.FieldKey

enum Field {

    ACOUSTID_FINGERPRINT(FieldKey.ACOUSTID_FINGERPRINT, 'AcoustID fingerprint', false),
    ACOUSTID_ID(FieldKey.ACOUSTID_ID, 'AcoustID ID', false),
    ALBUM(FieldKey.ALBUM, 'album', true),
    ALBUM_ARTIST(FieldKey.ALBUM_ARTIST, 'album artist', true),
    ALBUM_ARTIST_SORT(FieldKey.ALBUM_ARTIST_SORT, 'album artist sort', false),
    ALBUM_SORT(FieldKey.ALBUM_SORT, 'album sort', false),
    AMAZON_ID(FieldKey.AMAZON_ID, 'Amazon ID', false),
    ARRANGER(FieldKey.ARRANGER, 'arranger', false),
    ARTIST(FieldKey.ARTIST, 'artist', true),
    ARTIST_SORT(FieldKey.ARTIST_SORT, 'artist sort', false),
    ARTISTS(FieldKey.ARTISTS, 'artists', false),
    BARCODE(FieldKey.BARCODE, 'barcode', false),
    BPM(FieldKey.BPM, 'BPM', false),
    CATALOG_NO(FieldKey.CATALOG_NO, 'catalog number', false),
    COMMENT(FieldKey.COMMENT, 'comment', false),
    COMPOSER(FieldKey.COMPOSER, 'composer', false),
    COMPOSER_SORT(FieldKey.COMPOSER_SORT, 'composer sort', false),
    CONDUCTOR(FieldKey.CONDUCTOR, 'conductor', false),
    COUNTRY(FieldKey.COUNTRY, 'country', false),
    COVER_ART(FieldKey.COVER_ART, 'cover art', false),
    CUSTOM1(FieldKey.CUSTOM1, 'custom1', false),
    CUSTOM2(FieldKey.CUSTOM2, 'custom2', false),
    CUSTOM3(FieldKey.CUSTOM3, 'custom3', false),
    CUSTOM4(FieldKey.CUSTOM4, 'custom4', false),
    CUSTOM5(FieldKey.CUSTOM5, 'custom5', false),
    DISC_NO(FieldKey.DISC_NO, 'disc number', false),
    DISC_SUBTITLE(FieldKey.DISC_SUBTITLE, 'disc subtitle', false),
    DISC_TOTAL(FieldKey.DISC_TOTAL, 'discs total', false),
    DJMIXER(FieldKey.DJMIXER, 'DJ mixer', false),
    ENCODER(FieldKey.ENCODER, 'encoder', false),
    ENGINEER(FieldKey.ENGINEER, 'engineer', false),
    FBPM(FieldKey.FBPM, 'FBPM', false),
    GENRE(FieldKey.GENRE, 'genre', true),
    GROUPING(FieldKey.GROUPING, 'grouping', false),
    ISRC(FieldKey.ISRC, 'ISRC', false),
    IS_COMPILATION(FieldKey.IS_COMPILATION, 'is compilation', false),
    KEY(FieldKey.KEY, 'key', false),
    LANGUAGE(FieldKey.LANGUAGE, 'language', false),
    LYRICIST(FieldKey.LYRICIST, 'lyricist', false),
    LYRICS(FieldKey.LYRICS, 'lyrics', false),
    MEDIA(FieldKey.MEDIA, 'media', false),
    MIXER(FieldKey.MIXER, 'mixer', false),
    MOOD(FieldKey.MOOD, 'mood', false),
    MUSICBRAINZ_ARTISTID(FieldKey.MUSICBRAINZ_ARTISTID, 'MB artist ID', false),
    MUSICBRAINZ_DISC_ID(FieldKey.MUSICBRAINZ_DISC_ID, 'MB disc ID', false),
    MUSICBRAINZ_ORIGINAL_RELEASE_ID(FieldKey.MUSICBRAINZ_ORIGINAL_RELEASE_ID, 'MB original release ID', false),
    MUSICBRAINZ_RELEASEARTISTID(FieldKey.MUSICBRAINZ_RELEASEARTISTID, 'MB release artist ID', false),
    MUSICBRAINZ_RELEASEID(FieldKey.MUSICBRAINZ_RELEASEID, 'MB released', false),
    MUSICBRAINZ_RELEASE_COUNTRY(FieldKey.MUSICBRAINZ_RELEASE_COUNTRY, 'MB release country', false),
    MUSICBRAINZ_RELEASE_GROUP_ID(FieldKey.MUSICBRAINZ_RELEASE_GROUP_ID, 'MB release group ID', false),
    MUSICBRAINZ_RELEASE_STATUS(FieldKey.MUSICBRAINZ_RELEASE_STATUS, 'MB release status', false),
    MUSICBRAINZ_RELEASE_TRACK_ID(FieldKey.MUSICBRAINZ_RELEASE_TRACK_ID, 'MB release track ID', false),
    MUSICBRAINZ_RELEASE_TYPE(FieldKey.MUSICBRAINZ_RELEASE_TYPE, 'MB release type', false),
    MUSICBRAINZ_TRACK_ID(FieldKey.MUSICBRAINZ_TRACK_ID, 'MB track ID', false),
    MUSICBRAINZ_WORK_ID(FieldKey.MUSICBRAINZ_WORK_ID, 'MB work ID', false),
    MUSICIP_ID(FieldKey.MUSICIP_ID, 'MusicIP ID', false),
    OCCASION(FieldKey.OCCASION, 'occasion', false),
    ORIGINAL_ALBUM(FieldKey.ORIGINAL_ALBUM, 'original album', false),
    ORIGINAL_ARTIST(FieldKey.ORIGINAL_ARTIST, 'original artist', false),
    ORIGINAL_LYRICIST(FieldKey.ORIGINAL_LYRICIST, 'original lyricist', false),
    ORIGINAL_YEAR(FieldKey.ORIGINAL_YEAR, 'original year', false),
    QUALITY(FieldKey.QUALITY, 'quality', false),
    PRODUCER(FieldKey.PRODUCER, 'producer', false),
    RATING(FieldKey.RATING, 'rating', false),
    RECORD_LABEL(FieldKey.RECORD_LABEL, 'record label', false),
    REMIXER(FieldKey.REMIXER, 'remixer', false),
    SCRIPT(FieldKey.SCRIPT, 'script', false),
    SUBTITLE(FieldKey.SUBTITLE, 'subtitle', false),
    TAGS(FieldKey.TAGS, 'tags', false),
    TEMPO(FieldKey.TEMPO, 'tempo', false),
    TITLE(FieldKey.TITLE, 'title', true),
    TITLE_SORT(FieldKey.TITLE_SORT, 'title sort', false),
    TRACK(FieldKey.TRACK, 'track', true),
    TRACK_TOTAL(FieldKey.TRACK_TOTAL, 'track total', true),
    URL_DISCOGS_ARTIST_SITE(FieldKey.URL_DISCOGS_ARTIST_SITE, 'Discogs artist site', false),
    URL_DISCOGS_RELEASE_SITE(FieldKey.URL_DISCOGS_RELEASE_SITE, 'Discogs release site', false),
    URL_LYRICS_SITE(FieldKey.URL_LYRICS_SITE, 'Lyrics site', false),
    URL_OFFICIAL_ARTIST_SITE(FieldKey.URL_OFFICIAL_ARTIST_SITE, 'Official artist site', false),
    URL_OFFICIAL_RELEASE_SITE(FieldKey.URL_OFFICIAL_RELEASE_SITE, 'Official release site', false),
    URL_WIKIPEDIA_ARTIST_SITE(FieldKey.URL_WIKIPEDIA_ARTIST_SITE, 'Wikipedia artist site', false),
    URL_WIKIPEDIA_RELEASE_SITE(FieldKey.URL_WIKIPEDIA_RELEASE_SITE, 'Wikipedia release site', false),
    YEAR(FieldKey.YEAR, 'year', true)

    @Nonnull
    final FieldKey key
    @Nonnull
    final String desc
    final boolean required

    private Field(@Nonnull final FieldKey key, @Nonnull final String desc, final boolean required) {
        this.key = key
        this.desc = desc
        this.required = required
    }

    public static final Collection<Field> REQUIRED_FIELDS = values().findAll { it.required }
    public static final Collection<Field> EXTRANEOUS_FIELDS = values().findAll { !it.required }

}
