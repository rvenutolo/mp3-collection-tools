package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FORMAT
import static org.venutolo.mp3.Constants.TARGET_PIXELS

import groovy.util.logging.Slf4j
import java.awt.image.BufferedImage
import javax.annotation.Nonnull
import javax.annotation.Nullable
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractDirCheck
import org.venutolo.mp3.process.util.ImageUtil

@Slf4j
class AlbumImageCheck extends AbstractDirCheck {

    AlbumImageCheck(@Nonnull final Output output) {
        super(log, output, true)
    }

    @Override
    void checkInternal(@Nonnull final File dir) {
        def albumArtFile = new File(dir, ALBUM_IMAGE_FILENAME)
        log.debug('Checking album art: {}', albumArtFile.canonicalPath)
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
            output.write(albumArtFile, 'Not square', "${imageWidth}x${imageHeight}")
        }
        def imageFormat = getImageFormat(albumArtFile)
        if (imageFormat && imageFormat != ALBUM_IMAGE_FORMAT) {
            output.write(albumArtFile, 'Not expected image format', imageFormat)
        }
    }

    @Nullable
    private BufferedImage readImage(@Nonnull final File imageFile) {
        try {
            ImageUtil.readImage(imageFile)
        } catch (final Exception e) {
            output.write(imageFile, 'Exception when reading image', e.message)
            null
        }
    }

    @Nullable
    private String getImageFormat(@Nonnull final File imageFile) {
        try {
            ImageUtil.getImageFormat(imageFile)
        } catch (final Exception e) {
            output.write(imageFile, 'Exception when getting image format', e.message)
            null
        }
    }

}
