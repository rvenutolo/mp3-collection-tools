package org.venutolo.mp3.fix.impl

import static org.venutolo.mp3.Constants.ID3V2_TARGET_MAJOR_VERSION

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.fix.AbstractMp3FileFix
import org.venutolo.mp3.output.Output

@Slf4j
class TagTypeFix extends AbstractMp3FileFix {

    TagTypeFix(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    boolean fixInternal(@Nonnull final MP3File mp3File) {
        def fixed = false
        if (mp3File.hasID3v1Tag()) {
            log.debug("Removing ID3v1 tag: {}", mp3File.file.canonicalPath)
            mp3File.setID3v1Tag(null)
            output.write(mp3File, 'Removed ID3v1 tag')
            fixed = true
        }
        if (mp3File.hasID3v2Tag()) {
            final def v2MajorVersion = mp3File.getID3v2Tag().getMajorVersion()
            if (v2MajorVersion != ID3V2_TARGET_MAJOR_VERSION) {
                log.debug("Converting tag to ID3v2.4: {}", mp3File.file.canonicalPath)
                // TODO is there a better way to convert tag versions?
                mp3File.setID3v2Tag(mp3File.getID3v2TagAsv24())
                output.write(mp3File, 'Converted to ID3v2.4 tag')
                fixed = true
            }
        }
        fixed
    }
}
