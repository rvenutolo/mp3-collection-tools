package org.venutolo.mp3.check

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class GenericDirContentsCheck extends AbstractDirCheck {

    GenericDirContentsCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, false)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def containsDirs = dir.listFiles().any { it.isDirectory() }
        def containsFiles = dir.listFiles().any { it.isFile() }
        def containsMp3Files = dir.listFiles().any { it.name.toLowerCase().endsWith('mp3') }
        if (!containsDirs && !containsFiles) {
            warningOutput.write(dir, 'Empty directory')
        } else if (containsDirs && containsFiles) {
            warningOutput.write(dir, 'Contains both directories and files')
        } else if (containsFiles && !containsMp3Files) {
            warningOutput.write(dir, 'Contains no MP3 files')
        }
    }

}
