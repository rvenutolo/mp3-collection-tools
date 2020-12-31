package org.venutolo.mp3.check

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class GenericDirContentsCheck extends AbstractDirCheck {

    GenericDirContentsCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def containsDirs = dir.listFiles().any { it.isDirectory() }
        def containsFiles = dir.listFiles().any { !it.isDirectory() }
        if (!containsDirs && !containsFiles) {
            warningOutput.write(dir, 'Empty directory')
        }
        if (containsDirs && containsFiles) {
            warningOutput.write(dir, 'Contains both directories and files')
        }
    }

}
