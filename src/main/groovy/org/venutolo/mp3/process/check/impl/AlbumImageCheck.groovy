package org.venutolo.mp3.process.check.impl

import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FILENAME
import static org.venutolo.mp3.Constants.ALBUM_IMAGE_FORMAT
import static org.venutolo.mp3.Constants.TARGET_PIXELS
import static org.venutolo.mp3.process.util.ImageUtil.getImageFormat
import static org.venutolo.mp3.process.util.ImageUtil.readImage

import groovy.util.logging.Slf4j
import javax.annotation.Nonnull
import org.venutolo.mp3.Output
import org.venutolo.mp3.process.check.AbstractDirCheck

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
        def imageOptional = readImage(albumArtFile, output)
        if (!imageOptional.isPresent()) {
            return
        }
        def image = imageOptional.get()
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
        def imageFormatOptional = getImageFormat(albumArtFile, output)
        if (!imageFormatOptional.isPresent()) {
            return
        }
        def imageFormat = imageFormatOptional.get()
        if (imageFormat && imageFormat != ALBUM_IMAGE_FORMAT) {
            output.write(albumArtFile, 'Not expected image format', imageFormat)
        }
    }

}
