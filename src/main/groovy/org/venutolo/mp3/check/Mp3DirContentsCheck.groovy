package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class Mp3DirContentsCheck extends AbstractDirCheck {

    Mp3DirContentsCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def allFiles = dir.listFiles()
        def mp3Files = allFiles.findAll { it.name.toLowerCase().endsWith('.mp3') }
        def otherFiles = (allFiles - mp3Files).findAll { it.name != ALBUM_IMAGE_FILENAME }
        if (!mp3Files) {
            warningOutput.write(dir, 'No MP3 files')
        }
        mp3Files.findAll { !it.name.endsWith('.mp3') }.each {
            warningOutput.write(it, 'Non-lowercase file extension')
        }
        otherFiles.each {
            warningOutput.write(it, 'Unexpected file')
        }
    }

}
