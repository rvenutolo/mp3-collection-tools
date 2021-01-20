package org.venutolo.mp3.process.fix.impl

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME

import groovy.util.logging.Slf4j
import java.nio.file.Files
import javax.annotation.Nonnull
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.fix.AbstractDirFix

@Slf4j
class AlbumImageFilenameFix extends AbstractDirFix {

    AlbumImageFilenameFix(@Nonnull final Output output) {
        super(log, output, false)
    }

    @Override
    protected boolean fixInternal(@Nonnull final File dir) {
        def hasAlbumImageFile = dir.listFiles().any { file -> file.name == ALBUM_IMAGE_FILENAME }
        if (hasAlbumImageFile) {
            return false
        }
        def misnamedFiles = dir.listFiles().findAll { file -> badFileNameCase(file) || badFileExtension(file) }
        if (misnamedFiles.size() != 1) {
            return false
        }
        def fileToRename = misnamedFiles.first()
        Files.move(fileToRename.toPath(), fileToRename.toPath().resolveSibling(ALBUM_IMAGE_FILENAME))
        output.write(fileToRename, "Renamed to: ${ALBUM_IMAGE_FILENAME}")
        // This fix doesn't require writing mp3 tags, so return false, even if
        // a fix was made
        return false
    }

    private static boolean badFileNameCase(@Nonnull final File file) {
        // catch 'folder.jpg', 'FOLDER.JPG', etc
        return file.name.toLowerCase() == ALBUM_IMAGE_FILENAME.toLowerCase()
    }

    private static boolean badFileExtension(@Nonnull final File file) {
        // catch 'Folder.jpeg', 'folder.jpeg', etc
        return file.name.toLowerCase() == ALBUM_IMAGE_FILENAME.toLowerCase().replace('.jpg', '.jpeg')
    }

}
