package org.venutolo.mp3.check

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.TARGET_PIXELS

import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import javax.imageio.ImageIO
import org.venutolo.mp3.output.Output

@Slf4j
class AlbumImageCheck extends AbstractDirCheck {

    AlbumImageCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def albumArtFile = new File(dir, ALBUM_IMAGE_FILENAME)
        log.debug("Checking album art: {}", albumArtFile.canonicalPath)
        if (!albumArtFile.exists()) {
            output.write(dir, 'No album image')
            return
        }
        def image = readImage(albumArtFile)
        if (!image) {
            return
        }
        def imageWidth = image.getWidth()
        def imageHeight = image.getHeight()
        if (imageWidth < TARGET_PIXELS && imageHeight < TARGET_PIXELS) {
            output.write(albumArtFile, "Dimensions less than ${TARGET_PIXELS}", "${imageWidth}x${imageHeight}")
        }
        if (imageWidth > TARGET_PIXELS && imageHeight > TARGET_PIXELS) {
            output.write(albumArtFile, "Larger than ${TARGET_PIXELS}x${TARGET_PIXELS}", "${imageWidth}x${imageHeight}")
        }
        if (imageWidth != imageHeight) {
            output.write(albumArtFile, "Not square", "${imageWidth}x${imageHeight}")
        }
    }

    @Nullable
    private BufferedImage readImage(@Nonnull final File imageFile) {
        try {
            ImageIO.read(imageFile)
        } catch (final Exception e) {
            output.write(imageFile, 'Exception on ImageIO::read', e.message)
            null
        }
    }

}
