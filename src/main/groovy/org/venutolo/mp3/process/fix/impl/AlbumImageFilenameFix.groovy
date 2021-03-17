package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.core.Constants.ALBUM_IMAGE_FILENAME

import groovy.util.logging.Slf4j
import java.nio.file.Files
import javax.annotation.Nonnull
import org.venutolo.mp3.core.Output
import org.venutolo.mp3.process.fix.AbstractDirFix

@Slf4j
class AlbumImageFilenameFix extends AbstractDirFix {

    AlbumImageFilenameFix(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    protected boolean fixInternal(@Nonnull final File dir) {
        def albumImageFile = new File(dir, ALBUM_IMAGE_FILENAME)
        if (albumImageFile.exists()) {
            return false
        }
        def misnamedFiles = dir.listFiles().findAll { file -> badFileNameCase(file) || badFileExtension(file) }
        if (misnamedFiles.size() != 1) {
            return false
        }
        def fileToRename = misnamedFiles.first()
        Files.move(fileToRename.toPath(), albumImageFile.toPath())
        output.write(fileToRename, 'Renamed', ALBUM_IMAGE_FILENAME)
        // This fix doesn't require writing mp3 tags, so return false, even if
        // a fix was made
        false
    }

    private static boolean badFileNameCase(@Nonnull final File file) {
        // catch 'folder.jpg', 'FOLDER.JPG', etc
        file.name.toLowerCase() == ALBUM_IMAGE_FILENAME.toLowerCase()
    }

    private static boolean badFileExtension(@Nonnull final File file) {
        // catch 'Folder.jpeg', 'folder.jpeg', etc
        file.name.toLowerCase() == ALBUM_IMAGE_FILENAME.toLowerCase().replace('.jpg', '.jpeg')
    }

}
