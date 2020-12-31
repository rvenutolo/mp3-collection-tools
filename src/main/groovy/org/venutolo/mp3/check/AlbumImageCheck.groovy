package org.venutolo.mp3.check

import static java.util.Objects.requireNonNull

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import javax.imageio.ImageIO
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class AlbumImageCheck {

    private static final String ALBUM_IMAGE_FILENAME = 'Folder.jpg'
    private static final int TARGET_IMAGE_DIMENSION = 1000

    @Nonnull
    protected final WarningOutput warningOutput

    AlbumImageCheck(@Nonnull final WarningOutput warningOutput) {
        this.warningOutput = requireNonNull(warningOutput, 'Warning output cannot be null')
    }

    void check(@Nonnull final File dir) {
        log.debug('Checking album art in: {}', dir.canonicalPath)
        requireNonNull(dir, 'Directory cannot be null')
        if (!dir.isDirectory()) {
            throw new IllegalArgumentException("${dir.canonicalPath} is not a directory")
        }
        def albumArtFile = new File(dir, ALBUM_IMAGE_FILENAME)
        log.debug("Checking album art: {}", albumArtFile.canonicalPath)
        if (albumArtFile.exists()) {
            def image = ImageIO.read(albumArtFile)
            def imageWidth = image.getWidth()
            def imageHeight = image.getHeight()
            if (imageWidth < TARGET_IMAGE_DIMENSION && imageHeight < TARGET_IMAGE_DIMENSION) {
                warningOutput.write(
                    albumArtFile,
                    "Dimensions less than ${TARGET_IMAGE_DIMENSION}",
                    "${imageWidth}x${imageHeight}"
                )
            }
            if (imageWidth > TARGET_IMAGE_DIMENSION && imageHeight > TARGET_IMAGE_DIMENSION) {
                warningOutput.write(
                    albumArtFile,
                    "Larger than ${TARGET_IMAGE_DIMENSION}x${TARGET_IMAGE_DIMENSION}",
                    "${imageWidth}x${imageHeight}"
                )
            }
            if (imageWidth != imageHeight) {
                warningOutput.write(
                    albumArtFile,
                    "Not square",
                    "${imageWidth}x${imageHeight}"
                )
            }
        } else {
            warningOutput.write(dir, 'No album image')
        }
        log.debug('Checked album art in: {}', dir.canonicalPath)
    }

}
