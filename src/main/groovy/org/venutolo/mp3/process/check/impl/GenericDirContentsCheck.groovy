package org.venutolo.mp3.process.check.impl

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractDirCheck

@Slf4j
class GenericDirContentsCheck extends AbstractDirCheck {

    GenericDirContentsCheck(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def containsDirs = dir.listFiles().any { file -> file.isDirectory() }
        def containsFiles = dir.listFiles().any { file -> file.isFile() }
        def containsMp3Files = dir.listFiles().any { file -> file.name.toLowerCase().endsWith('mp3') }
        if (!containsDirs && !containsFiles) {
            output.write(dir, 'Empty directory')
        } else if (containsDirs && containsFiles) {
            output.write(dir, 'Contains both directories and files')
        } else if (containsFiles && !containsMp3Files) {
            output.write(dir, 'Contains no MP3 files')
        }
    }

}
