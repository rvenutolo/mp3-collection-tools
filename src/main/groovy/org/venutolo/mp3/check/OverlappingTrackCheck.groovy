package org.venutolo.mp3.check

import static org.venutolo.mp3.fields.Field.TRACK

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.Output

@Slf4j
class OverlappingTrackCheck extends AbstractMultipleMp3FilesCheck {

    OverlappingTrackCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    protected void checkInternal(@Nonnull final Collection<MP3File> mp3Files, @Nonnull final File dir) {
        def seenTrackNumbers = [:].withDefault { 0 } as Map<String, Integer>
        mp3Files.each { mp3File ->
            def track = mp3File.getID3v2TagAsv24().getFirst(TRACK.key)
            if (track) {
                seenTrackNumbers[track] += 1
            }
        }
        seenTrackNumbers.findAll { it.value > 1 }.keySet().sort().each { key ->
            output.write(dir, "Multiple track #${key}")
        }
    }

}
