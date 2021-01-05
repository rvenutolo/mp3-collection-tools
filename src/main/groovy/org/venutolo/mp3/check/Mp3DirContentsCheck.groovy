package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.output.Output

@Slf4j
class Mp3DirContentsCheck extends AbstractDirCheck {

    Mp3DirContentsCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def allFiles = dir.listFiles().findAll { it.isFile() }
        def mp3Files = allFiles.findAll { it.name.toLowerCase().endsWith('.mp3') }
        def otherFiles = (allFiles - mp3Files).findAll { it.name != ALBUM_IMAGE_FILENAME }
        mp3Files.findAll { !it.name.endsWith('.mp3') }.each {
            output.write(it, 'Non-lowercase file extension')
        }
        otherFiles.each {
            output.write(it, 'Unexpected file')
        }
    }

}
