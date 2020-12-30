package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class MissingTrackCheck extends AbstractMultipleMp3FilesCheck {

    MissingTrackCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def seenTrackNumbers = [] as Set<Integer>
        mp3Files.each { mp3File ->
            def track = mp3File.getID3v2Tag().getFirst(TRACK.key)
            if (track) {
                seenTrackNumbers << (track as int)
            }
        }
        if (!seenTrackNumbers) {
            return
        }
        def maxTrackNumber = seenTrackNumbers.max()
        (1..maxTrackNumber).each { trackNumber ->
            if (!seenTrackNumbers.contains(trackNumber)) {
                warningOutput.write(dir, "Missing track #${trackNumber}")
            }
        }
    }

}
