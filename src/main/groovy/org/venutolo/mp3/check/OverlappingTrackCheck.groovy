package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class OverlappingTrackCheck extends AbstractMultipleMp3FilesCheck {

    OverlappingTrackCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def seenTrackNumbers = [:].withDefault { 0 } as Map<String, Integer>
        mp3Files.each { mp3File ->
            def track = mp3File.getID3v2Tag().getFirst(TRACK.key)
            if (track) {
                seenTrackNumbers[track] += 1
            }
        }
        seenTrackNumbers.findAll { it.value > 1 }.keySet().sort().each { key ->
            warningOutput.write(dir, "Multiple track #${key}")
        }
    }

}
