package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.ID3V2_TARGET_VERSION

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractMp3FileFix

@Slf4j
class TagTypeFix extends AbstractMp3FileFix {

    TagTypeFix(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    boolean fixInternal(@Nonnull final Mp3File mp3File) {
        def fixed = false
        if (mp3File.hasID3v1Tag() && mp3File.hasID3v2Tag()) {
            log.debug("Removing ID3v1 tag: {}", mp3File.canonicalPath())
            mp3File.removeID3v1Tag()
            output.write(mp3File, 'Removed ID3v1 tag')
            fixed = true
        }
        if (mp3File.hasID3v2Tag()) {
            final def v2Version = mp3File.getID3v2Tag().getVersion()
            if (v2Version != ID3V2_TARGET_VERSION) {
                log.debug("Converting tag to ID3v{}: {}", ID3V2_TARGET_VERSION, mp3File.canonicalPath())
                mp3File.convertID3v2VersionTo24()
                output.write(mp3File, "Converted to ID3v${ID3V2_TARGET_VERSION} tag")
                fixed = true
            }
        }
        fixed
    }

}
