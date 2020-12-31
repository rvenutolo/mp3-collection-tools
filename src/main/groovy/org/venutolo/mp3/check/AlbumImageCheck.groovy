package org.venutolo.mp3.check

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import javax.imageio.ImageIO
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class AlbumImageCheck extends AbstractDirCheck {

    private static final String ALBUM_IMAGE_FILENAME = 'Folder.jpg'
    private static final int TARGET_IMAGE_DIMENSION = 1000

    AlbumImageCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
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
    }

}
