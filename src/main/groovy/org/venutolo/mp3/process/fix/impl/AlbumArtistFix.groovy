package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Field.ALBUM_ARTIST
import static org.venutolo.mp3.core.Field.ARTIST

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractMultipleMp3FilesFix

@Slf4j
class AlbumArtistFix extends AbstractMultipleMp3FilesFix {

    AlbumArtistFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir) {
        def anyMissingAlbumArtist = mp3Files
            .any { mp3File -> !mp3File.getID3v2Tag().has(ALBUM_ARTIST) }
        if (!anyMissingAlbumArtist) {
            return false
        }
        def trackArtists = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().get(ARTIST) }
            .findAll { artist -> !artist.isEmpty() }
            .unique() as List<String>
        if (!trackArtists || trackArtists.size() > 1) {
            return false
        }
        def trackArtist = trackArtists.first()
        def anyAlbumArtistDifferentThanTrackArtist = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().get(ALBUM_ARTIST) }
            .findAll { albumArtist -> !albumArtist.isEmpty() }
            .any { albumArtist -> albumArtist && albumArtist != trackArtist }
        if (anyAlbumArtistDifferentThanTrackArtist) {
            return false
        }
        log.debug('Writing {}: {} for MP3s in : {}', ALBUM_ARTIST, trackArtist, dir.canonicalPath)
        mp3Files
            .findAll { mp3File -> mp3File.getID3v2Tag().get(ALBUM_ARTIST) != trackArtist }
            .each { mp3File ->
                mp3File.getID3v2Tag().set(ALBUM_ARTIST, trackArtist)
                output.write(mp3File, "Wrote: ${ALBUM_ARTIST}", trackArtist)
            }
        true
    }

}
