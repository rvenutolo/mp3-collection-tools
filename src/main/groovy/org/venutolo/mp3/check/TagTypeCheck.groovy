package org.venutolo.mp3.check

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.jaudiotagger.audio.mp3.MP3File
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class TagTypeCheck extends AbstractMp3FileCheck {

    private static final int ID3V2_TARGET_MAJOR_VERSION = 4

    TagTypeCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, false)
    }

    @Override
    void checkInternal(@Nonnull final MP3File mp3File) {
        if (mp3File.hasID3v1Tag()) {
            warningOutput.write(mp3File, 'Has ID3v1 tag')
        }
        if (mp3File.hasID3v2Tag()) {
            final def v2MajorVersion = mp3File.getID3v2Tag().getMajorVersion()
            if (v2MajorVersion != ID3V2_TARGET_MAJOR_VERSION) {
                warningOutput.write(mp3File, 'ID3v2 tag is not v2.4', "v2.${v2MajorVersion}")
            }
        } else {
            warningOutput.write(mp3File, 'Does not have ID3v2 tag')
        }
    }

}
