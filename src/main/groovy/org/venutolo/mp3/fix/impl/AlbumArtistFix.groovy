package org.venutolo.mp3.fix.impl

import static org.venutolo.mp3.fields.Field.ALBUM_ARTIST
import static org.venutolo.mp3.fields.Field.ARTIST

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fix.AbstractMultipleMp3FilesFix
import org.venutolo.mp3.output.Output

@Slf4j
class AlbumArtistFix extends AbstractMultipleMp3FilesFix {

    AlbumArtistFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def anyMissingAlbumArtist = mp3Files
            .any { mp3File -> !mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) }
        if (!anyMissingAlbumArtist) {
            return false
        }
        def trackArtists = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getAll(ARTIST.key) as List<String> }
            .flatten()
            .unique() as List<String>
        if (!trackArtists || trackArtists.size() > 1) {
            return false
        }
        def trackArtist = trackArtists.first()
        def anyAlbumArtistDifferentThanTrackArtist = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getAll(ALBUM_ARTIST.key) }
            .any { albumArtists -> !albumArtists.isEmpty() && albumArtists != [trackArtist] }
        if (anyAlbumArtistDifferentThanTrackArtist) {
            return false
        }
        log.debug('Writing {}: {} for MP3s in : {}', ALBUM_ARTIST.desc, trackArtist, dir.canonicalPath)
        mp3Files
            .findAll { mp3File -> mp3File.getID3v2Tag().getFirst(ALBUM_ARTIST.key) != trackArtist }
            .each { mp3File ->
                mp3File.getID3v2Tag().setField(ALBUM_ARTIST.key, trackArtist)
                output.write(mp3File, "Wrote: ${ALBUM_ARTIST.desc}", trackArtist)
            }
        return true
    }


}