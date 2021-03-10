package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Field.TRACK
import static org.venutolo.mp3.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.fix.AbstractMultipleMp3FilesFix

@Slf4j
class TrackTotalFix extends AbstractMultipleMp3FilesFix {

    TrackTotalFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def allHaveTrackTotal = mp3Files.every { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) }
        if (allHaveTrackTotal) {
            return false
        }
        def seenTotalTrackNumbers = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) }
            .findAll { s -> !s.isEmpty() }
            .toSet()
        def hasMultipleTotalTrackNumbers = seenTotalTrackNumbers.size() > 1
        if (hasMultipleTotalTrackNumbers) {
            return false
        }
        def seenTrackNumbers = mp3Files
            .collect { mp3File -> mp3File.getID3v2Tag().getFirst(TRACK.key) }
            .findAll { s -> !s.isEmpty() }
            .collect { trackNum -> trackNum as Integer }
            .toSet()
        def hasMissingOrOverlappingTrackNums = !seenTrackNumbers.containsAll((1..mp3Files.size()))
        if (hasMissingOrOverlappingTrackNums) {
            return false
        }
        def maxTrackNumber = seenTrackNumbers.max() as String
        log.debug('Writing {}: {} for MP3s in : {}', TRACK_TOTAL.desc, maxTrackNumber, dir.canonicalPath)
        mp3Files
            .findAll { mp3File -> !mp3File.getID3v2Tag().getFirst(TRACK_TOTAL.key) }
            .each { mp3File ->
                mp3File.getID3v2Tag().setField(TRACK_TOTAL.key, maxTrackNumber)
                output.write(mp3File, "Wrote: ${TRACK_TOTAL.desc}", maxTrackNumber)
            }
        true
    }

}
