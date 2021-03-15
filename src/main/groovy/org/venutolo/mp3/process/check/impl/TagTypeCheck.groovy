package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.core.Constants.ID3V2_TARGET_VERSION

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Mp3File
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.check.AbstractMp3FileCheck

@Slf4j
class TagTypeCheck extends AbstractMp3FileCheck {

    TagTypeCheck(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    void checkInternal(@Nonnull final Mp3File mp3File) {
        if (mp3File.hasID3v1Tag()) {
            output.write(mp3File, 'Has ID3v1 tag')
        }
        if (mp3File.hasID3v2Tag()) {
            final def v2Version = mp3File.getID3v2Tag().getVersion()
            if (v2Version != ID3V2_TARGET_VERSION) {
                output.write(mp3File, "ID3v2 tag is not v${ID3V2_TARGET_VERSION}", "v${v2Version}")
            }
        } else {
            output.write(mp3File, 'Does not have ID3v2 tag')
        }
    }

}
