package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Field.TRACK
import static org.venutolo.mp3.core.Field.TRACK_TOTAL

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractMultipleMp3FilesFix

@Slf4j
final class TrackTotalFix extends AbstractMultipleMp3FilesFix {

    TrackTotalFix(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected boolean fixInternal(@Nonnull final Collection<Mp3File> mp3Files, @Nonnull final File dir) {
        def allHaveTrackTotal = mp3Files.every { mp3File -> mp3File.getId3v2Tag().has(TRACK_TOTAL) }
        if (allHaveTrackTotal) {
            return false
        }
        def seenTotalTrackNumbers = mp3Files
            .collect { mp3File -> mp3File.getId3v2Tag().get(TRACK_TOTAL) }
            .findAll { s -> !s.isEmpty() }
            .toSet()
        def hasMultipleTotalTrackNumbers = seenTotalTrackNumbers.size() > 1
        if (hasMultipleTotalTrackNumbers) {
            return false
        }
        def seenTrackNumbers = mp3Files
            .collect { mp3File -> mp3File.getId3v2Tag().get(TRACK) }
            .findAll { s -> !s.isEmpty() }
            .collect { trackNum -> trackNum as Integer }
            .toSet()
        def hasMissingOrOverlappingTrackNums = !seenTrackNumbers.containsAll((1..mp3Files.size()))
        if (hasMissingOrOverlappingTrackNums) {
            return false
        }
        def maxTrackNumber = seenTrackNumbers.max() as String
        log.debug('Writing {}: {} for MP3s in : {}', TRACK_TOTAL, maxTrackNumber, dir.canonicalPath)
        mp3Files
            .findAll { mp3File -> !mp3File.getId3v2Tag().has(TRACK_TOTAL) }
            .each { mp3File ->
                mp3File.getId3v2Tag().set(TRACK_TOTAL, maxTrackNumber)
                output.write(mp3File, "Wrote: ${TRACK_TOTAL}", maxTrackNumber)
            }
        true
    }

}
