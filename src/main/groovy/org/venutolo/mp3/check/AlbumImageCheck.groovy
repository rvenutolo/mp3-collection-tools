package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.TARGET_IMAGE_DIMENSION

import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import javax.imageio.ImageIO
import org.venutolo.mp3.output.WarningOutput

@Slf4j
class AlbumImageCheck extends AbstractDirCheck {

    AlbumImageCheck(@Nonnull final WarningOutput warningOutput) {
        super(log, warningOutput, true)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def albumArtFile = new File(dir, ALBUM_IMAGE_FILENAME)
        log.debug("Checking album art: {}", albumArtFile.canonicalPath)
        if (albumArtFile.exists()) {
            def image = readImage(albumArtFile)
            if (image) {
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
            }
        } else {
            warningOutput.write(dir, 'No album image')
        }
    }

    @Nullable
    private BufferedImage readImage(@Nonnull final File imageFile) {
        try {
            return ImageIO.read(imageFile)
        } catch (final Exception e) {
            warningOutput.write(imageFile, 'Exception on ImageIO::read', e.message)
            return null
        }
    }

}
