package org.venutolo.mp3.check.impl

import static org.venutolo.mp3.Constants.ID3V2_TARGET_MAJOR_VERSION

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.check.AbstractMp3FileCheck
import org.venutolo.mp3.output.Output

@Slf4j
class TagTypeCheck extends AbstractMp3FileCheck {

    TagTypeCheck(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    void checkInternal(@Nonnull final MP3File mp3File) {
        if (mp3File.hasID3v1Tag()) {
            output.write(mp3File, 'Has ID3v1 tag')
        }
        if (mp3File.hasID3v2Tag()) {
            final def v2MajorVersion = mp3File.getID3v2Tag().getMajorVersion()
            if (v2MajorVersion != ID3V2_TARGET_MAJOR_VERSION) {
                output.write(mp3File, 'ID3v2 tag is not v2.4', "v2.${v2MajorVersion}")
            }
        } else {
            output.write(mp3File, 'Does not have ID3v2 tag')
        }
    }

}
